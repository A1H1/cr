package `in`.devco.cr.ui.home

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseMVVMActivity
import `in`.devco.cr.data.model.User
import `in`.devco.cr.ui.reportcrime.ReportCrimeActivity
import `in`.devco.cr.util.LocationListener
import `in`.devco.cr.util.checkLocationPermissions
import `in`.devco.cr.util.getLocationUpdate
import android.content.Context
import android.content.Intent
import android.location.Location
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.button_action.*


class HomeActivity : BaseMVVMActivity<User, HomeViewModel>(), OnMapReadyCallback, LocationListener,
    NavigationView.OnNavigationItemSelectedListener {
    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    private lateinit var mapFragment: SupportMapFragment
    private val disposable = CompositeDisposable()
    private var googleMap: GoogleMap? = null
    private var haveLocationPermission = false

    override fun layoutRes() = R.layout.activity_home

    override fun init() {
        super.init()
        actionTextView.setText(R.string.emergency)
        setupNavigationView()

        mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        disposable.clear()
        disposable.add(checkLocationPermissions(this, this))
    }

    override fun setData(data: User) {

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
    }

    override fun onPermissionGranted() {
        haveLocationPermission = true
        googleMap?.isMyLocationEnabled = haveLocationPermission

        disposable.clear()
        disposable.add(getLocationUpdate(this, this))
    }

    override fun onLocationFound(location: Location) {
        Toast.makeText(this, "${location.latitude}, ${location.longitude}", Toast.LENGTH_LONG)
            .show()

        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), 19F
            )
        )
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
}
