package com.projectinfinity.app.listeners

import com.projectinfinity.app.ui.pojo.UserListResponse

interface StudentAdapterListener {
     fun onImageClick(imageUrl:String,position:Int)
    fun onSendClick(studentDetails: UserListResponse.Data, position: Int)

}