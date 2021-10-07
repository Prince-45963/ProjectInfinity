package com.projectinfinity.app.dashboardbottom.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.projectinfinity.app.R
import com.projectinfinity.app.server.RetrofitClient
import com.projectinfinity.app.ui.pojo.SingleUserDataResponse
import com.projectinfinity.app.utils.GeneralUtils.displayToast
import com.projectinfinity.app.utils.GeneralUtils.getLoader
import com.projectinfinity.app.utils.SharedPreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_user_profile_details.view.*
import kotlinx.android.synthetic.main.fragment_profile.iv_profile_logo
import kotlinx.android.synthetic.main.fragment_profile.ll_profile_details_profile_fragment
import kotlinx.android.synthetic.main.fragment_profile.tv_email_profile_fragment
import kotlinx.android.synthetic.main.fragment_profile_color.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileColorFragment : Fragment(R.layout.fragment_profile_color) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        getSingleUserData(2)
        toolbar_fragment_profile_color.title = getString(R.string.Profile_dashboard_title)
    }


    @SuppressLint("SetTextI18n")
    private fun singleUserInfoDialog(
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
        if (dialog!!.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        dialog.show()
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun getSingleUserData(userId: Int) {
        //loaders make it easy to asynchronously load data
        val loader = requireActivity().getLoader(getString(R.string.loader_loading))
        //showing loader
        loader.show()
        val getUserDataApiCall = RetrofitClient.create().getSingleData(userId)
        getUserDataApiCall.enqueue(object : Callback<SingleUserDataResponse> {
            override fun onResponse(
                call: Call<SingleUserDataResponse>,
                response: Response<SingleUserDataResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    //loader hide
                    loader.hide()

                    //calling colorDetails function
                    colorDetails(response.body()?.data!!)
                } else {
                    requireContext().displayToast("Something went wrong!!1")
                }
            }

            override fun onFailure(call: Call<SingleUserDataResponse>, t: Throwable) {
                requireActivity().displayToast(t.localizedMessage)
            }


        })
    }

    fun colorDetails(data: SingleUserDataResponse.Data) {
        tv_email_profile_fragment.text = SharedPreferenceManager.email
        Picasso.get().load(data.avatar).into(iv_profile_logo)
        ll_profile_details_profile_fragment.setOnClickListener {
            singleUserInfoDialog(
                SharedPreferenceManager.email.toString(),
                data.firstName,
                data.lastName,
                data.avatar
            )
        }

    }


}