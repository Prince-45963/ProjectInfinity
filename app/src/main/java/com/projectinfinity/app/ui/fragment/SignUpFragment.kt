import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.ktx.options
import com.projectinfinity.app.R
import com.projectinfinity.app.dashboardbottom.ui.activity.BottomDashboardActivity
import com.projectinfinity.app.ui.activity.DashboardActivity
import com.projectinfinity.app.ui.pojo.SignUpUserDetails
import com.projectinfinity.app.utils.Constants.KEY_EMAIL
import com.projectinfinity.app.utils.GeneralUtils.databaseReference
import com.projectinfinity.app.utils.GeneralUtils.displayToast
import com.projectinfinity.app.utils.GeneralUtils.firebaseAuth
import com.projectinfinity.app.utils.GeneralUtils.isEmailValid
import com.projectinfinity.app.utils.GeneralUtils.isPasswordValid
import com.projectinfinity.app.utils.GeneralUtils.isPhoneNumberValid
import com.projectinfinity.app.utils.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        btn_sign_up_sign_up_fragment.setOnClickListener {
            //initialize the auth
            firebaseAuth()
            //accessing data from edit text
            val firstName = et_first_name.text.toString()
            val lastName = et_last_name.text.toString()
            val phoneNumber = et_phone_number.text.toString()
            val occupation = et_occupation.text.toString()
            val password = et_password_sign_up.text.toString()
            val email = et_email_sign_up.text.toString()

            if (isInputDataValid(
                    firstName,
                    lastName,
                    phoneNumber,
                    password,
                    occupation,
                    email
                )
            ) {
                createAccount(email, password)
                requireActivity().supportFragmentManager.commit {
                    //Passing email to DashboardActivity with the help of intent
                    SharedPreferenceManager.idPref = firebaseAuth().uid
                    val signUpIntent =
                        Intent(requireContext(), BottomDashboardActivity::class.java).putExtra(
                            KEY_EMAIL,
                            email
                        )
                    startActivity(signUpIntent)
                    addToBackStack(SignUpFragment::class.java.name)
                }
            }
        }
        //If you click on Sign up then you will go to SignUpFragment
        tv_sign_in_sign_up_fragment.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<SignInFragment>(R.id.fcv_activity_launcher)
                addToBackStack(SignInFragment::class.java.name)
            }
        }
    }
    fun createAccount(email: String,password: String){
        firebaseAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            if(it.isSuccessful){
                val uid=it.result!!.user!!.uid
                databaseReference().child("users").child(uid).setValue(SignUpUserDetails(et_first_name.text.toString(),et_last_name.text.toString(),et_phone_number.text.toString(),et_occupation.text.toString(),et_email_sign_up.text.toString(),uid))

            }
        }

    }


    //Details validation function
    private fun isInputDataValid(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        password: String,
        occupation: String,
        email: String,
    ): Boolean {
        if (firstName.isEmpty()) {
            requireContext().displayToast(getString(R.string.Please_First_Name))
            return false
        } else if (lastName.isEmpty()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Last_Name))
            return false
        } else if (phoneNumber.isEmpty()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Phone_Number))
            return false

        } else if (phoneNumber.isPhoneNumberValid()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Valid_Phone_Number))
            return false

        } else if (email.isEmpty()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Your_Email))
            return false

        } else if (!email.isEmailValid()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Valid_Email))
            return false

        } else if (occupation.isEmpty()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Occupation))
            return false

        } else if (password.isEmpty()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Valid_Occupation))
            return false

        }else if (!password.isPasswordValid()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Valid_Password))
            return false

        } else {
            return true
        }
    }

}