package com.projectinfinity.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.projectinfinity.app.R
import com.projectinfinity.app.listeners.DialogShowListener
import com.projectinfinity.app.server.RetrofitClient
import com.projectinfinity.app.ui.fragment.ProfileFragment
import com.projectinfinity.app.ui.fragment.WelcomeFragment
import com.projectinfinity.app.ui.pojo.SingleUserDataResponse
import com.projectinfinity.app.utils.Constants.KEY_EMAIL
import com.projectinfinity.app.utils.Constants.KEY_FIRST_NAME
import com.projectinfinity.app.utils.Constants.KEY_LAST_NAME
import com.projectinfinity.app.utils.Constants.KEY_PROFILE
import com.projectinfinity.app.utils.GeneralUtils.displayToast
import com.projectinfinity.app.utils.GeneralUtils.firebaseAuth
import com.projectinfinity.app.utils.GeneralUtils.generalDialog
import com.projectinfinity.app.utils.GeneralUtils.getLoader
import com.projectinfinity.app.utils.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar_dashboard)


        //accessing intent value
        val firstName = intent.getStringExtra(KEY_FIRST_NAME)
        val email = intent.getStringExtra(KEY_EMAIL)


        supportFragmentManager.commit {
            setReorderingAllowed(true)
            //send bundle to welcome fragment
            val detailBundle = bundleOf(KEY_FIRST_NAME to firstName, KEY_EMAIL to email)
            add<WelcomeFragment>(R.id.fcv_activity_dashboard, args = detailBundle)
        }
    }


    //This function will inflate menu_item
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        //sets the toolbar title
        toolbar_dashboard.title = "Dashboard"
        return true

    }

    //This function will help to select the menu_item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_profile -> {

                getSingleUserData(2)
                true
            }
            R.id.menu_logout -> {
                //Create an alert builder
                this.generalDialog("Logout", "Do you want to logout?", object :
                    DialogShowListener {

                    //setPositiveButton function is override here
                    override fun setPositiveButton(title: String, message: String) {
                        SharedPreferenceManager.clearPreference()
                        firebaseAuth().signOut()
                        val launcherIntent =
                            Intent(applicationContext, LauncherActivity::class.java)
                        //Intent is used to switch from one activity to another.
                        startActivity(launcherIntent)
                        //Finish activity
                        finish()
                    }

                    //setNegativeButton function is override here
                    override fun setNegativeButton() {

                    }
                })
                true
            }

            else -> {
                false
            }
        }
    }

    private fun getSingleUserData(userId: Int) {
        //loaders make it easy to asynchronously load data
        val loader = this.getLoader("Loading..")
        loader.show()
        val getUserDataApiCall = RetrofitClient.create().getSingleData(userId)
        getUserDataApiCall.enqueue(object : Callback<SingleUserDataResponse> {
            override fun onResponse(
                call: Call<SingleUserDataResponse>,
                response: Response<SingleUserDataResponse>
            ) {
                val userDetailsBundle = detailsBundle(response.body()?.data!!)
                loader.hide()
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<ProfileFragment>(
                        R.id.fcv_activity_dashboard,
                        args = userDetailsBundle
                    ).addToBackStack(DashboardActivity::class.java.name)
                }

            }

            override fun onFailure(call: Call<SingleUserDataResponse>, t: Throwable) {
                displayToast("error in api")
            }


        })
    }

    private fun detailsBundle(data: SingleUserDataResponse.Data): Bundle? {

        val profile: String = data.avatar
        val firstName: String = data.firstName
        val lastName: String = data.lastName
        val email: String = data.email
        val detailBundle = bundleOf(
            KEY_PROFILE to profile, KEY_FIRST_NAME to firstName, KEY_LAST_NAME to lastName,
            KEY_EMAIL to email
        )
        return detailBundle
    }


}

