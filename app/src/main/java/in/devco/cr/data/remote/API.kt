package `in`.devco.cr.data.remote

import `in`.devco.cr.data.model.BaseResponse
import `in`.devco.cr.data.model.User
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}