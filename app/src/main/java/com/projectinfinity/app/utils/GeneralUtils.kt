package com.projectinfinity.app.utils

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.projectinfinity.app.listeners.DialogShowListener
import java.util.regex.Matcher
import java.util.regex.Pattern
import android.provider.MediaStore

import android.graphics.Bitmap
import android.net.Uri
import java.io.ByteArrayOutputStream


object GeneralUtils {
    /**
     * Method to check email
     * is valid or not
     * true if valid
     * false if not valid
     */
    fun String.isEmailValid(): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(this)
        return matcher.matches()
    }

    /**
     * Method to check password length
     * is valid or not
     * true if valid
     * false if not valid
     */
    fun String.isPasswordValid(): Boolean {
        return this.length >= 3
    }

    fun String.isPhoneNumberValid(): Boolean {
        return this.length != 10
    }

    /**
     * Method to show toast of [text]
     */
    fun Context.displayToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * Method to show logout Dialog
     */

    fun Context.generalDialog(title: String, message: String, callback: DialogShowListener) {
        //Create an alert builder

        val builder = AlertDialog.Builder(this)

        //set the title
        builder.setTitle(title)
        //set the message
        builder.setMessage(message)
        //if click on "yes" button
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            callback.setPositiveButton(title, message)

        }
        //if click on "No" button
        builder.setNegativeButton("No") { dialogInterface, which ->
            callback.setNegativeButton()
            dialogInterface.dismiss()
        }
        val dialog: AlertDialog? = builder.create()


        dialog?.show()

    }

    fun Context.getLoader(message: String): ProgressDialog {

        val valName = ProgressDialog(this)
        valName.setMessage(message)
        valName.setCancelable(false)
        valName.setInverseBackgroundForced(false)
        return valName
    }

    //firebaseAuth function
    fun firebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    fun databaseReference(): DatabaseReference {
        return Firebase.database.reference
    }
    fun getDatabaseReference(path:String):DatabaseReference{
        return Firebase.database.getReference(path)

    }
    fun isConnectionAvailable(requireContext: Context):Boolean{
        val cm = requireContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
    //getImage Uri
    fun getImageUri(requireContext: Context, iImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        iImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        var uri:Uri?=null
        var path =
            MediaStore.Images.Media.insertImage(requireContext.contentResolver, iImage, "Title", null)
        if(path!=null){
              uri=Uri.parse(path)
            }
        return uri
    }

}

