package `in`.devco.cr.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val userId: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("token") val token: String,
    @SerializedName("role") val userTye: UserTye
)

enum class UserTye {
    @SerializedName("police")
    POLICE,

    @SerializedName("citizen")
    CITIZEN
}