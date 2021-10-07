package com.projectinfinity.app.ui.pojo


import com.google.gson.annotations.SerializedName

data class LoginApiResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("token")
    val token: String
)