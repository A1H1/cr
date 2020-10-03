package `in`.devco.cr.ui.crimelist

import `in`.devco.cr.base.BaseViewModel
import `in`.devco.cr.data.model.DataWrapper
import `in`.devco.cr.data.model.Report
import `in`.devco.cr.data.repository.CrimeRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CrimeListViewModel @Inject constructor(private val repository: CrimeRepository) :
    BaseViewModel<List<Report>>() {
    private val _tracking = MutableLiveData<DataWrapper<Boolean>>()
    val tracking: LiveData<DataWrapper<Boolean>> = _tracking
    private val _alert = MutableLiveData<DataWrapper<Boolean>>()
    val alert: LiveData<DataWrapper<Boolean>> = _alert

    fun getAllReport() {
        data.postValue(DataWrapper(isLoading = true))
        disposable.add(
            repository
                .getPendingReports()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    data.postValue(DataWrapper(response = it))
                }, {
                    data.postValue(DataWrapper(exception = it))
                })
        )
    }

    fun startTracking(currentUserId: String, reporterId: String, reportId: String) {
        _tracking.postValue(DataWrapper(isLoading = true))
        disposable.add(
            repository
                .startTracking(currentUserId, reporterId, reportId)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _tracking.postValue(DataWrapper(response = true))
                }, {
                    _tracking.postValue(DataWrapper(exception = it))
                })
        )
    }

    fun alert(reportId: String) {
        _alert.postValue(DataWrapper(isLoading = true))
        disposable.add(
            repository
                .alert(reportId)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _alert.postValue(DataWrapper(response = true))
                }, {
                    _alert.postValue(DataWrapper(exception = it))
                })
        )
    }
}