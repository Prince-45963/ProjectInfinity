package com.projectinfinity.app.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceManager {
    const val SHARED_PREFERENCES_KEY="shared_preferences_key"
     var EMAIL="email"
    const val KEY_TOKEN="key_token"
    const val KEY_ID="key_id"

    const val PASSWORD="password"
    private var sharePre: SharedPreferences?=null
    fun initPref(context: Context){
        if(sharePre==null){
            sharePre=context.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE)
        }
    }

    fun clearPreference()= sharePre!!.edit()?.clear()?.apply()


    var tokenPref:String?
        get()= sharePre?.getString(KEY_TOKEN,"")
        set(tokenPref)= sharePre?.edit()?.putString(KEY_TOKEN,tokenPref!!)!!.apply()
    var idPref:String?
        get()= sharePre?.getString(KEY_ID,"")
        set(idPref)= sharePre?.edit()?.putString(KEY_ID,idPref!!)!!.apply()
    var email: String?
       get()=sharePre?.getString(EMAIL,"")

       set(email)= sharePre?.edit()?.putString(EMAIL,email)!!.apply()
    }

