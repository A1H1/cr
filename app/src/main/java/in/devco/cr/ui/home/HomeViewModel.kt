package `in`.devco.cr.ui.home

import `in`.devco.cr.base.BaseViewModel
import `in`.devco.cr.data.model.DataWrapper
import `in`.devco.cr.data.repository.CrimeRepository
import android.location.Location
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: CrimeRepository) :
    BaseViewModel<Boolean>() {
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
                    data.postValue(DataWrapper(response = true))
                }, {
                    data.postValue(DataWrapper(exception = it))
                })
        )
    }
}