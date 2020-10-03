package `in`.devco.cr.ui.home

import `in`.devco.cr.base.BaseViewModel
import `in`.devco.cr.data.model.DataWrapper
import `in`.devco.cr.data.model.LatLong
import `in`.devco.cr.data.repository.CrimeRepository
import `in`.devco.cr.data.repository.UserRepository
import `in`.devco.cr.util.SharedPref
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: CrimeRepository,
    private val userRepository: UserRepository
) :
    BaseViewModel<LatLng>() {
    private val _redZone = MutableLiveData<DataWrapper<List<LatLong>>>()
    val redZone: LiveData<DataWrapper<List<LatLong>>> = _redZone
    private lateinit var userId: String

    fun setUserId(userId: String) {
        this.userId = userId
    }

    fun updateLocation(location: Location) {
        disposable.add(
            repository
                .updateLocation(location, userId)
                .subscribeOn(Schedulers.io())
                .subscribe({
                }, {
                    data.postValue(DataWrapper(exception = it))
                })
        )
    }

    fun getLocation(userId: String) {
        disposable.add(
            repository
                .getLocation(userId)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    data.postValue(DataWrapper(response = it))
                }, {
                    data.postValue(DataWrapper(exception = it))
                })
        )
    }

    fun getRedZone(latLng: LatLng) {
        disposable.add(
            repository
                .getRedZone(latLng.latitude.toString(), latLng.longitude.toString())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _redZone.postValue(DataWrapper(response = it))
                }, {
                    data.postValue(DataWrapper(exception = it))
                })
        )
    }

    fun updateToken() {
        userRepository.updateFCMToken(SharedPref.getFCMToken().orEmpty()).enqueue(object :
            Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {}
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
        })
    }
}