package `in`.devco.cr.util

import `in`.devco.cr.BuildConfig
import `in`.devco.cr.R
import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

interface LocationListener {
    fun onPermissionGranted()
    fun onLocationFound(location: Location)
    fun onLocationNotFound()
}

fun checkLocationPermissions(fragment: Fragment, locationListener: LocationListener): Disposable {
    val rxPermissions = RxPermissions(fragment)
    rxPermissions.setLogging(true)

    return rxPermissions
        .requestEachCombined(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        .subscribe { permission ->
            when {
                permission.granted -> locationListener.onPermissionGranted()
                else -> {
                    MaterialDialog.Builder(fragment.requireContext())
                        .title(R.string.title_settings_dialog)
                        .content(R.string.rationale_ask_again)
                        .positiveText(R.string.allow)
                        .negativeText(R.string.deny)
                        .onPositive { _, _ ->
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                                fragment.startActivity(this)
                            }
                        }
                        .onNegative { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
        }
}

fun getLocationUpdate(context: Context, locationListener: LocationListener): Disposable {
    val reactiveLocationProvider = ReactiveLocationProvider(context)

    val locationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setFastestInterval(30000)
        .setInterval(60000)

    return reactiveLocationProvider
        .checkLocationSettings(
            LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)
                .build()
        )
        .subscribeOn(Schedulers.newThread())
        .flatMap { reactiveLocationProvider.getUpdatedLocation(locationRequest) }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ location ->
            locationListener.onLocationFound(location)
        }) { throwable ->
            locationListener.onLocationNotFound()
            Log.e("ERROR", "${throwable.message}")
        }
}