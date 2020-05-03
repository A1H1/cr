package `in`.devco.cr.data.remote

import `in`.devco.cr.data.model.BaseResponse
import `in`.devco.cr.data.model.User
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("report")
    fun reportCrime(
        @Part("lat") latitude: String,
        @Part("long") longitude: String,
        @Part("desc") description: String,
        @Part imageFile: MultipartBody.Part?,
        @Part videoFile: MultipartBody.Part?
    ) : Completable
}