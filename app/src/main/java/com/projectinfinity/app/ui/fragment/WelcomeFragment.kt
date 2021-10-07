package com.projectinfinity.app.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectinfinity.app.R
import com.projectinfinity.app.listeners.DialogShowListener
import com.projectinfinity.app.listeners.StudentAdapterListener
import com.projectinfinity.app.server.RetrofitClient
import com.projectinfinity.app.ui.activity.LauncherActivity
import com.projectinfinity.app.ui.adapter.StudentClassAdapter
import com.projectinfinity.app.ui.pojo.SingleUserDataResponse
import com.projectinfinity.app.ui.pojo.UserListResponse
import com.projectinfinity.app.ui.pojo.UserListResponse.Data
import com.projectinfinity.app.utils.Constants.KEY_EMAIL
import com.projectinfinity.app.utils.Constants.KEY_FIRST_NAME
import com.projectinfinity.app.utils.GeneralUtils.displayToast
import com.projectinfinity.app.utils.GeneralUtils.generalDialog
import com.projectinfinity.app.utils.GeneralUtils.getLoader
import com.projectinfinity.app.utils.SharedPreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_user_data_show.view.*
import kotlinx.android.synthetic.main.fragment_welcome.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WelcomeFragment : Fragment(com.projectinfinity.app.R.layout.fragment_welcome) {
    val adapter =
        StudentClassAdapter(
            mutableListOf<Data>(),
            object : StudentAdapterListener {
                override fun onImageClick(imageUrl: String, position: Int) {
                    //requireContext().displayToast("Image click")
                    // Create an alert builder
                    val builder = AlertDialog.Builder(requireContext())
                    // set the custom layout
                    val customLayout: View = layoutInflater
                        .inflate(
                            R.layout.student_dialog, null
                        )
                    val image: ImageView = customLayout.iv_image_dialog
                    builder.setView(customLayout)
                    Picasso.get()
                        .load(imageUrl)
                        .into(image)
                    val okButton: Button = customLayout.btn_yes_dialog
                    val cancelButton: Button = customLayout.btn_cancel_dialog
                    val dialog: AlertDialog? = builder.create()
                    dialog?.show()
                    okButton.setOnClickListener {
                        dialog?.dismiss()
                    }
                    cancelButton.setOnClickListener {
                        dialog?.dismiss()
                    }
                }

                override fun onSendClick(studentDetails: Data, position: Int) {
                    getSingleData(studentDetails.id)

                }


            })

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //accessing data from bundles keys
        val email = arguments?.getString(KEY_EMAIL)
        val firstName = arguments?.getString(KEY_FIRST_NAME)
        val llm = LinearLayoutManager(requireContext())
        rv_student_list.layoutManager = llm
        getUserList(1)
        rv_student_list.adapter = adapter
    }

    private fun getUserList(page: Int) {
        val loader=requireActivity().getLoader("Loading..")
        loader.show()
        val getUserListApiCall = RetrofitClient.create().getData(page)
        getUserListApiCall.enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    loader.hide()
                    adapter.dataUpdate(response.body()?.data!!)
                } else {
                    requireContext().displayToast("Something went wrong!!1")
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {

            }

        })
    }

    private fun getSingleData(userId: Int) {
        requireActivity().generalDialog("User Details","Do you want to see user detail?",object :
            DialogShowListener {


            override fun setPositiveButton(title: String, message: String) {
                val loader = ProgressDialog(context)
                loader.setMessage("User Details")
                loader.setCancelable(false)
                loader.setInverseBackgroundForced(false)
                loader.show()
                val getUserDataApiCall = RetrofitClient.create().getSingleData(userId)
                getUserDataApiCall.enqueue(object : Callback<SingleUserDataResponse> {
                    override fun onResponse(
                        call: Call<SingleUserDataResponse>,
                        response: Response<SingleUserDataResponse>
                    ) {
                        loader.hide()
                        singleUserDataDialog(response)
                    }

                    override fun onFailure(call: Call<SingleUserDataResponse>, t: Throwable) {
                        requireContext().displayToast("error in api")
                    }


                })
            }

            override fun setNegativeButton() {

            }
        })

    }

    private fun singleUserDataDialog(response: Response<SingleUserDataResponse>) {
        val builder = AlertDialog.Builder(requireContext())
        // set the custom layout
        val customLayout: View = layoutInflater
            .inflate(
                R.layout.dialog_user_data_show, null
            )
        val image: ImageView = customLayout.iv_user_image
        builder.setView(customLayout)
        Picasso.get()
            .load(response.body()?.data?.avatar)
            .into(image)
        val email: TextView = customLayout.tv_user_email
        email.text = response.body()?.data?.email
        val username: TextView = customLayout.tv_user_name
        username.text = response.body()?.data?.firstName + " " + response.body()?.data?.lastName
        val cancelButton: Button = customLayout.btn_user_dialog_close
        val dialog: AlertDialog? = builder.create()
        dialog?.show()
        cancelButton.setOnClickListener {
            dialog?.dismiss()
        }
    }


}

