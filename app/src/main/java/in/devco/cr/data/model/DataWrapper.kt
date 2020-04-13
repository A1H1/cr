package `in`.devco.cr.data.model

data class DataWrapper<T>(
    val response: T? = null,
    val isLoading: Boolean = false,
    val error: ErrorResponse? = null,
    val errorCode: Int? = null,
    val inputError: Int? = null,
    val exception: Throwable? = null
)