package `in`.devco.cr.ui.reportcrime

import `in`.devco.cr.base.BaseViewModel
import `in`.devco.cr.data.model.DataWrapper
import `in`.devco.cr.data.repository.CrimeRepository
import `in`.devco.cr.util.AppConst.INPUT_ERROR_DESCRIPTION
import `in`.devco.cr.util.AppConst.INPUT_ERROR_FILE
import android.location.Location
import android.util.Log
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


class ReportCrimeViewModel @Inject constructor(private val repository: CrimeRepository) :
    BaseViewModel<Boolean>() {

    fun reportCrime(description: String, location: Location, image: String?, video: String?) {
        data.postValue(DataWrapper(isLoading = true))

        if (description.length < 3) {
            data.postValue(DataWrapper(inputError = INPUT_ERROR_DESCRIPTION))
            return
        } else if (image == null && video == null) {
            data.postValue(DataWrapper(inputError = INPUT_ERROR_FILE))
            return
        }

        val imageFile = if (image == null) null else File(image)

        val imagePart = if (imageFile == null) null else MultipartBody.Part.createFormData(
            "reportImage",
            imageFile.name, imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        )

        val videoFile = if (video == null) null else File(video)

        val videoPart = if (videoFile == null) null else MultipartBody.Part.createFormData(
            "reportImage2",
            videoFile.name, videoFile.asRequestBody("video/*".toMediaTypeOrNull())
        )

        disposable.add(
            repository
                .reportCrime(
                    location.latitude,
                    location.longitude,
                    description,
                    imagePart,
                    videoPart
                )
                .subscribeOn(Schedulers.io())
                .subscribe({
                    data.postValue(DataWrapper(response = true))
                }, {
                    data.postValue(DataWrapper(exception = it))
                    Log.e("Error", it.message.orEmpty())
                })
        )
    }
}