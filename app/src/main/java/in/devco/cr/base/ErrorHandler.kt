package `in`.devco.cr.base

import `in`.devco.cr.data.model.ErrorResponse

interface ErrorHandler {
    fun error(throwable: Throwable)
    fun error(error: ErrorResponse)
    fun error(code: Int)
    fun authError()
    fun noInternet()
    fun displayError()
    fun serverError()
    fun inputError(code: Int)
}