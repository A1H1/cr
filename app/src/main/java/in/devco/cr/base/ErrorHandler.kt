package `in`.devco.cr.base

interface ErrorHandler {
    fun error(throwable: Throwable)
    fun error(code: Int)
    fun authError()
    fun noInternet()
    fun displayError()
    fun serverError()
    fun inputError(code: Int)
}