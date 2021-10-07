import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.projectinfinity.app.R
import com.projectinfinity.app.dashboardbottom.ui.activity.BottomDashboardActivity
import com.projectinfinity.app.ui.activity.DashboardActivity
import com.projectinfinity.app.utils.Constants.KEY_EMAIL
import com.projectinfinity.app.utils.GeneralUtils.displayToast
import com.projectinfinity.app.utils.GeneralUtils.firebaseAuth
import com.projectinfinity.app.utils.GeneralUtils.getLoader
import com.projectinfinity.app.utils.GeneralUtils.isEmailValid
import com.projectinfinity.app.utils.GeneralUtils.isPasswordValid
import com.projectinfinity.app.utils.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //On Sign_in Button click
        btn_sign_in_sign_in_fragment.setOnClickListener {
            //loaders make it easy to asynchronously load data
            val loader = requireActivity().getLoader("Loading..")
            loader.show()
            //accessing data from edit text
            val email = et_email_address.text.toString()
            val password = et_password.text.toString()

            if (isSignInDetailsValid(email, password)) {

                firebaseAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {


                        loader.hide()

                        SharedPreferenceManager.idPref = firebaseAuth().uid
                        val intent =
                            Intent(
                                requireContext(),
                                BottomDashboardActivity::class.java
                            ).putExtra(
                                KEY_EMAIL,
                                email
                            )

                        startActivity(intent)
                    } else {
                        requireActivity().displayToast("something wrong in credentials")
                    }
                }
            }


        }
        tv_sign_up_sign_in_fragment.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<SignUpFragment>(R.id.fcv_activity_launcher)
                addToBackStack(SignUpFragment::class.java.name)

            }
        }

    }

    //Sign in details validation
    private fun isSignInDetailsValid(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Your_Email))
            return false
        } else if (!email.isEmailValid()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Valid_Email))
            return false
        } else if (password.isEmpty()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Your_Password))
            return false

        } else if (!password.isPasswordValid()) {
            requireContext().displayToast(getString(R.string.Please_Enter_Valid_Password))
            return false

        } else {
            return true
        }
    }

}