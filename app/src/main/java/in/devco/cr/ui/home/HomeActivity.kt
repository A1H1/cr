package `in`.devco.cr.ui.home

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseMVVMActivity
import `in`.devco.cr.data.model.LatLong
import `in`.devco.cr.extensions.hide
import `in`.devco.cr.extensions.show
import `in`.devco.cr.ui.auth.login.LoginActivity
import `in`.devco.cr.ui.crimelist.CrimeListActivity
import `in`.devco.cr.ui.reportcrime.ReportCrimeActivity
import `in`.devco.cr.util.LocationListener
import `in`.devco.cr.util.SharedPref.clearTrackingUser
import `in`.devco.cr.util.SharedPref.getTrackingUser
import `in`.devco.cr.util.SharedPref.logout
import `in`.devco.cr.util.checkLocationPermissions
import `in`.devco.cr.util.getLocationUpdate
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import butterknife.OnClick
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.button_action.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

class HomeActivity : BaseMVVMActivity<LatLng, HomeViewModel>(), OnMapReadyCallback,
    LocationListener,
    NavigationView.OnNavigationItemSelectedListener {
    companion object {
        private const val EXTRA_FCM = "EXTRA_FCM"
        fun launch(context: Context, updateFCM: Boolean = false) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(EXTRA_FCM, updateFCM)
            context.startActivity(intent)
        }
    }

    private lateinit var mapFragment: SupportMapFragment
    private val disposable = CompositeDisposable()
    private var googleMap: GoogleMap? = null
    private var haveLocationPermission = false
    private var haveMarker = false
    private var isLocationShareRequired = false
    private var isRedZoneVisible = false
    private val markerOptions = MarkerOptions()
    private var otherLocation: LatLng? = null
    private var currentLocation: LatLng? = null
    private var marker: Marker? = null

    override fun layoutRes() = R.layout.activity_home

    override fun init() {
        super.init()
        clearTrackingUser()
        actionTextView.setText(R.string.emergency)
        setupNavigationView()

        mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.setUserId(getUser()?.userId.orEmpty())

        disposable.clear()
        disposable.add(checkLocationPermissions(this, this))

        if (intent.getBooleanExtra(EXTRA_FCM, false)) {
            viewModel.updateToken()
        }

        if (isPolice) {
            actionButton.hide()
        }
    }

    override fun observable() {
        super.observable()

        viewModel.redZone.observe(this, Observer { response ->
            response.response?.let { drawRedZone(it) }
        })
    }

    override fun setData(data: LatLng) {
        navigateButton.show()
        otherLocation = data
        marker?.position = data
        if (!haveMarker) {
            haveMarker = true
            markerOptions.position(data)

            markerOptions.icon(bitMapFromVector(if (isPolice) R.drawable.ic_circle else R.drawable.ic_police))
            googleMap?.clear()
            marker = googleMap?.addMarker(markerOptions)
        }

        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(data, 19F))

        currentLocation?.let {
            drawPrimaryLinePath(listOf(it, data))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return
        }
        googleMap.isMyLocationEnabled = haveLocationPermission
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START, true)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    private fun setupNavigationView() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        navigationView.getHeaderView(0).nameTextVew.text = getUser()?.name
        navigationView.getHeaderView(0).emailTextView.text = getUser()?.email

        if (!isPolice) {
            navigationView.menu.findItem(R.id.nav_all_report).isVisible = false
        }
    }

    override fun onPermissionGranted() {
        haveLocationPermission = true
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return
        }
        googleMap?.isMyLocationEnabled = haveLocationPermission

        disposable.clear()
        disposable.add(getLocationUpdate(this, this))
    }

    override fun onLocationFound(location: Location) {
        currentLocation = LatLng(location.latitude, location.longitude)

        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), 19F
            )
        )

        if (isLocationShareRequired) {
            viewModel.updateLocation(location)

            otherLocation?.let {
                drawPrimaryLinePath(listOf(currentLocation!!, it))
            }

        } else if (getTrackingUser().orEmpty().isNotEmpty()) {
            navigateButton.show()
            actionTextView.setText(R.string.stop_location_sharing)
            isLocationShareRequired = true
            viewModel.getLocation(getTrackingUser().orEmpty())
            clearTrackingUser()
        }
    }

    override fun onLocationNotFound() {
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_report_crime -> ReportCrimeActivity.launch(this)
            R.id.nav_all_report -> CrimeListActivity.launch(this)
        }
        return false
    }

    @OnClick(R.id.actionButton)
    fun emergency() {
        if (isPolice) {
            actionButton.hide()
        }
        actionTextView.setText(if (isLocationShareRequired) R.string.emergency else R.string.stop_location_sharing)
        isLocationShareRequired = !isLocationShareRequired

        if (!isLocationShareRequired) {
            navigateButton.hide()
            marker?.remove()
            haveMarker = false
        }
    }

    @OnClick(R.id.redZoneButton)
    fun redZone() {
        if (isRedZoneVisible) {
            googleMap?.clear()
        } else {
            currentLocation?.let { viewModel.getRedZone(it) }
        }
    }

    @OnClick(R.id.navigateButton)
    fun startNavigation() {
        otherLocation?.let {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?${if (currentLocation != null) "saddr=${currentLocation?.latitude},${currentLocation?.longitude}" else ""}&daddr=${otherLocation?.latitude},${otherLocation?.longitude}")
            )
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            logout().apply {
                LoginActivity.launch(this@HomeActivity).apply {
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CrimeListActivity.REQUEST_CODE -> {
                    isLocationShareRequired = true
                    actionButton.show()
                    actionTextView.setText(R.string.stop_location_sharing)
                    viewModel.getLocation(
                        data?.getStringExtra(CrimeListActivity.EXTRA_USER_ID).orEmpty()
                    )
                }
            }
        }
    }

    private fun drawPrimaryLinePath(locations: List<LatLng>) {
//        val options = PolylineOptions()
//        options.color(Color.parseColor("#CC0000FF"))
//        options.width(5f)
//        options.visible(true)
//        locations.forEach { options.add(it) }
//        googleMap?.addPolyline(options)
    }

    private fun drawRedZone(locations: List<LatLong>) {
        googleMap?.clear()
        locations.forEach {
            val markerOptions = MarkerOptions()
            markerOptions.position(LatLng(it.lat.toDouble(), it.long.toDouble()))
            markerOptions.icon(bitMapFromVector(R.drawable.ic_circle_red))
            googleMap?.addMarker(markerOptions)
        }
    }
}
