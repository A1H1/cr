package `in`.devco.cr.data.model

import com.google.gson.annotations.SerializedName

data class Report(
    @SerializedName("id") val id: String,
    @SerializedName("lat") val latitude: String,
    @SerializedName("long") val longitude: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("desc") val description: String,
    @SerializedName("status") val status: String,
    @SerializedName("files") val files: List<String>
)