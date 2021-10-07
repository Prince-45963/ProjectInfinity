package com.projectinfinity.app.ui.activity

import SignInFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.projectinfinity.app.R

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            //adding SignInFragment with launcher activity
            add<SignInFragment>(R.id.fcv_activity_launcher)

        }

    }
}

