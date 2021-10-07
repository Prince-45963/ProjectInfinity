package com.projectinfinity.app.server

import com.projectinfinity.app.ui.pojo.ColorResponse
import com.projectinfinity.app.ui.pojo.LoginApiResponse
import com.projectinfinity.app.ui.pojo.SingleUserDataResponse
import com.projectinfinity.app.ui.pojo.UserListResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @GET("users")
    fun getData(@Query("page")page:Int): Call<UserListResponse>
    @GET("users/{userId}")
    fun getSingleData(@Path("userId")id:Int): Call<SingleUserDataResponse>
    @FormUrlEncoded
    @POST("register")
     fun getLoginCredentialData(@Field("email")email:String,@Field("password")Password:String):Call<LoginApiResponse>
    @GET("unknown")
    fun getColorData(): Call<ColorResponse>
}