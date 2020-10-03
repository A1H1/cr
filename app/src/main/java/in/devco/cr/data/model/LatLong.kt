package `in`.devco.cr.data.model

import com.google.gson.annotations.SerializedName

data class LatLong(
    @SerializedName("lat")
    val lat: String,
    @SerializedName("long")
    val long: String
)