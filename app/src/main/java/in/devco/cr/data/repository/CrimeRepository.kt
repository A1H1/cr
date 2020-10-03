package `in`.devco.cr.data.repository

import `in`.devco.cr.data.remote.API
import `in`.devco.cr.di.module.NetworkModule.provideClient
import `in`.devco.cr.di.module.NetworkModule.provideService
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Observable
import okhttp3.MultipartBody
import javax.inject.Inject

class CrimeRepository @Inject constructor() {
    private var service: API = provideService(provideClient())
    private val db = Firebase.firestore

    fun reportCrime(
        latitude: Double,
        longitude: Double,
        description: String,
        imageFile: MultipartBody.Part?,
        videoFile: MultipartBody.Part?
    ) = service.reportCrime(latitude, longitude, description, imageFile, videoFile)

    fun updateLocation(location: Location, userId: String) = service.updateLocation(
        location.latitude.toString(),
        location.longitude.toString(),
        userId
    )

    fun getPendingReports() = service.getPendingReports()

    fun getLocation(userId: String): Observable<LatLng> {
        return Observable.create { emitter ->
            val listenerRegistration = db.collection("Tracker").document(userId)
                .addSnapshotListener(EventListener { snapshot, e ->
                    if (e != null) {
                        emitter.onError(e)
                        return@EventListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        emitter.onNext(
                            LatLng(
                                snapshot["lat"].toString().toDouble(),
                                snapshot["long"].toString().toDouble()
                            )
                        )
                    } else {
                        emitter.onError(Throwable("No Data"))
                    }
                })
            emitter.setCancellable { listenerRegistration.remove() }
        }
    }

    fun startTracking(currentUserId: String, reporterId: String, reportId: String) =
        service.startTracking(currentUserId, reporterId, reportId)

    fun getRedZone(lat: String, long: String) = service.getRedZone(lat, long)

    fun alert(reportId: String) = service.alert(reportId)
}