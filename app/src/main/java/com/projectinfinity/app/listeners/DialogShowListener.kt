package com.projectinfinity.app.listeners

interface DialogShowListener {
    fun setPositiveButton(title:String,message:String)
    fun setNegativeButton()
}