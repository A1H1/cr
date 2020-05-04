package `in`.devco.cr.ui.home

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseMVVMActivity
import `in`.devco.cr.ui.auth.login.LoginActivity
import `in`.devco.cr.ui.reportcrime.ReportCrimeActivity
import `in`.devco.cr.util.LocationListener
import `in`.devco.cr.util.SharedPref.logout
import `in`.devco.cr.util.checkLocationPermissions
import `in`.devco.cr.util.getLocationUpdate
import android.content.Context
import android.content.Intent
import android.location.Location
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import butterknife.OnClick
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.button_action.*
import kotlinx.android.synthetic.main.nav_header_home.view.*


class HomeActivity : BaseMVVMActivity<Boolean, HomeViewModel>(), OnMapReadyCallback,
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
    private var isLocationShareRequired = false

    override fun layoutRes() = R.layout.activity_home

    override fun init() {
        super.init()
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
    }

    override fun setData(data: Boolean) {

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
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
    }

    override fun onPermissionGranted() {
        haveLocationPermission = true
        googleMap?.isMyLocationEnabled = haveLocationPermission

        disposable.clear()
        disposable.add(getLocationUpdate(this, this))
    }

    override fun onLocationFound(location: Location) {
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
        }
        return false
    }

    @OnClick(R.id.actionButton)
    fun emergency() {
        isLocationShareRequired = true
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
}
