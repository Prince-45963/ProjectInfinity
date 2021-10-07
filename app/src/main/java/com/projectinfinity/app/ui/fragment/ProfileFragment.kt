package com.projectinfinity.app.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.projectinfinity.app.R
import com.projectinfinity.app.listeners.DialogShowListener
import com.projectinfinity.app.ui.activity.LauncherActivity
import com.projectinfinity.app.utils.Constants.KEY_EMAIL
import com.projectinfinity.app.utils.Constants.KEY_FIRST_NAME
import com.projectinfinity.app.utils.Constants.KEY_LAST_NAME
import com.projectinfinity.app.utils.Constants.KEY_PROFILE
import com.projectinfinity.app.utils.GeneralUtils.generalDialog
import com.projectinfinity.app.utils.SharedPreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.dialog_user_profile_details.view.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val email = arguments?.getString(KEY_EMAIL)
        val profile = arguments?.getString(KEY_PROFILE)
        val firstName = arguments?.getString(KEY_FIRST_NAME)
        val lastName = arguments?.getString(KEY_LAST_NAME)


        tv_email_profile_fragment.text = email
        Picasso.get().load(profile).into(iv_profile_logo)
        ll_profile_details_profile_fragment.setOnClickListener {
            SingleUserInfoDialog(
                email.toString(),
                firstName.toString(),
                lastName.toString(),
                profile.toString()
            )
        }
    }

    //This function will inflate menu_item
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.profile_menu_items, menu)
        //sets the toolbar title
        requireActivity().toolbar_dashboard?.title = "Profile"

    }

    //This function will help to select the menu_item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile_menu_dashboard -> {
                requireActivity().supportFragmentManager.popBackStack()
                true
            }
            R.id.profile_menu_logout -> {
                //Create an alert builder
                requireActivity().generalDialog("Logout","Do you want to logout?",object :DialogShowListener{

                    override fun setPositiveButton(title: String, message: String) {
                        SharedPreferenceManager.clearPreference()
                        val launcherIntent = Intent(requireContext(), LauncherActivity::class.java)
                        //Intent is used to switch from one activity to another.
                        launcherIntent.action = Intent.FLAG_ACTIVITY_CLEAR_TASK.toString()
                        startActivity(launcherIntent)
                        requireActivity().finish()
                    }

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


    @SuppressLint("SetTextI18n")
    fun SingleUserInfoDialog(
        emailAddress: String,
        firstName: String,
        lastName: String,
        profile: String
    ) {


        // Create an alert builder
        val builder = AlertDialog.Builder(requireContext())
        // set the custom layout
        val customLayout: View = layoutInflater
            .inflate(
                R.layout.dialog_user_profile_details, null
            )
        val image: ImageView = customLayout.iv_user_profile_details_image
        builder.setView(customLayout)
        Picasso.get()
            .load(profile)
            .into(image)

        val email: TextView = customLayout.tv_user_profile_details_email
        email.text = emailAddress
        val username: TextView = customLayout.tv_user_profile_details_name
        username.text = "$firstName $lastName"
        val closeButton: Button = customLayout.btn_user_profile_details_dialog_close
        val dialog: AlertDialog? = builder.create()
        dialog?.show()
        closeButton.setOnClickListener {
            dialog?.dismiss()
        }
    }

}