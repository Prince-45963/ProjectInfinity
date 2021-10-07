package com.projectinfinity.app.dashboardbottom.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.projectinfinity.app.R
import com.projectinfinity.app.dashboardbottom.ui.fragment.TodoListFragment
import com.projectinfinity.app.dashboardbottom.ui.fragment.ProfileColorFragment
import com.projectinfinity.app.listeners.DialogShowListener
import com.projectinfinity.app.ui.activity.LauncherActivity
import com.projectinfinity.app.utils.GeneralUtils.generalDialog
import com.projectinfinity.app.utils.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_bottom_dashobard.*


class BottomDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_dashobard)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            //adding SignInFragment with launcher activity
            add<TodoListFragment>(R.id.fcv_activity_bottom_dashboard)
        }
            val bottomNavigationView:BottomNavigationView=bnv_bottom_dashboard
            bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem? ->
               when(item?.itemId){
                   R.id.menu_bottom_profile -> {
                       supportFragmentManager.commit {
                           setReorderingAllowed(true)
                           replace<ProfileColorFragment>(R.id.fcv_activity_bottom_dashboard)
                       }
                       true
                   }
                  R.id.menu_bottom_colors_list -> {
                      supportFragmentManager.commit {
                          setReorderingAllowed(true)
                        replace<TodoListFragment>(R.id.fcv_activity_bottom_dashboard)
                      }
                          true
                   }
                  R.id.menu_bottom_logout -> {


                      //Create an alert builder
                      this.generalDialog("Logout","Do you want to logout?",object :
                          DialogShowListener {

                          //setPositiveButton function is override here
                          override fun setPositiveButton(title: String, message: String) {
                              SharedPreferenceManager.clearPreference()
                              val launcherIntent = Intent(applicationContext, LauncherActivity::class.java)
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
               } //call here


        }
    }



//    private fun getSingleUserData(userId: Int) {
//        //loaders make it easy to asynchronously load data
//        val loader=this.getLoader("Loading..")
//        loader.show()
//        val getUserDataApiCall = RetrofitClient.create().getSingleData(userId)
//        getUserDataApiCall.enqueue(object : Callback<SingleUserDataResponse> {
//            override fun onResponse(
//                call: Call<SingleUserDataResponse>,
//                response: Response<SingleUserDataResponse>
//            ) {
//                val userDetailsBundle = detailsBundle(response.body()?.data!!)
//                loader.hide()
//                supportFragmentManager.commit {
//                    setReorderingAllowed(true)
//                    add<ProfileColorFragment>(
//                        R.id.fcv_activity_bottom_dashboard,
//                        args = userDetailsBundle
//                    ).addToBackStack(DashboardActivity::class.java.name)
//                }
//
//            }
//
//            override fun onFailure(call: Call<SingleUserDataResponse>, t: Throwable) {
//                displayToast("error in api")
//            }
//
//
//        })
//    }
//
//    private fun detailsBundle(data: SingleUserDataResponse.Data): Bundle? {
//
//        val profile: String = data.avatar
//        val firstName: String = data.firstName
//        val lastName: String = data.lastName
//        val email: String = data.email
//        val detailBundle = bundleOf(
//            Constants.KEY_PROFILE to profile, Constants.KEY_FIRST_NAME to firstName, Constants.KEY_LAST_NAME to lastName,
//            Constants.KEY_EMAIL to email
//        )
//        return detailBundle
//    }
}