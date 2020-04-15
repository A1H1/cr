package `in`.devco.cr.data.model

import com.google.gson.annotations.SerializedName

data class ReportCrime(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("token") val token: String
)