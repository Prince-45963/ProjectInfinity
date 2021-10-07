package com.projectinfinity.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.projectinfinity.app.R
import com.projectinfinity.app.dashboardbottom.ui.activity.BottomDashboardActivity
import com.projectinfinity.app.utils.Constants.SPLASH_SCREEN_TIME_OUT
import com.projectinfinity.app.utils.SharedPreferenceManager


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        SharedPreferenceManager.initPref(this)
        Handler().postDelayed(Runnable {
            //if user is not login then id would be 0
            val p=SharedPreferenceManager.idPref
            Log.e("TAG","$p")
            if(SharedPreferenceManager.idPref!=""){
                val intent = Intent(this, BottomDashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val launcherIntent = Intent(this, LauncherActivity::class.java)
                //Intent is used to switch from one activity to another.
                startActivity(launcherIntent)
                finish()
            }

            //the current activity will get finished.
        }, SPLASH_SCREEN_TIME_OUT)
    }

}