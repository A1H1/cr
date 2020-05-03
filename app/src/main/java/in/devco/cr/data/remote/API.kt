package `in`.devco.cr.data.remote

import `in`.devco.cr.data.model.BaseResponse
import `in`.devco.cr.data.model.User
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface API {
    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Single<BaseResponse<User>>

    @FormUrlEncoded
    @POST("users/signup")
    fun signUp(@FieldMap data: Map<String, String>): Single<BaseResponse<User>>

    @POST("users/savedevicetoken")
    @FormUrlEncoded
    fun updateUserFCM(@Field("device_token") fcmId: String): Call<Void>

    @Multipart
    @POST("report")
    fun reportCrime(
        @Part("lat") latitude: String,
        @Part("long") longitude: String,
        @Part("desc") description: String,
        @Part imageFile: MultipartBody.Part?,
        @Part videoFile: MultipartBody.Part?
    ): Completable

    @FormUrlEncoded
    @POST("police/starttracker")
    fun updateLocation(
        @Field("lat") latitude: String,
        @Field("long") longitude: String,
        @Field("user_id") userId: String
    ): Completable
}