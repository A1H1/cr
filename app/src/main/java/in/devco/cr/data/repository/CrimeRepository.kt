package `in`.devco.cr.data.repository

import `in`.devco.cr.data.remote.API
import `in`.devco.cr.di.module.NetworkModule.provideClient
import `in`.devco.cr.di.module.NetworkModule.provideService
import android.location.Location
import okhttp3.MultipartBody
import javax.inject.Inject

class CrimeRepository @Inject constructor() {
    private var service: API = provideService(provideClient())
    fun reportCrime(
        latitude: String,
        longitude: String,
        description: String,
        imageFile: MultipartBody.Part?,
        videoFile: MultipartBody.Part?
    ) = service.reportCrime(latitude, longitude, description, imageFile, videoFile)

    fun updateLocation(location: Location, userId: String) = service.updateLocation(
        location.latitude.toString(),
        location.longitude.toString(),
        userId
    )
}