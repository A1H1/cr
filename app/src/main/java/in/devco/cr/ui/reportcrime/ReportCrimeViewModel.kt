package `in`.devco.cr.ui.reportcrime

import `in`.devco.cr.base.BaseViewModel
import `in`.devco.cr.data.model.ReportCrime
import `in`.devco.cr.data.model.User
import `in`.devco.cr.data.repository.UserRepository
import javax.inject.Inject

class ReportCrimeViewModel @Inject constructor(private val repository: UserRepository) :
    BaseViewModel<ReportCrime>() {
}