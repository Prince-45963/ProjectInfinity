package com.projectinfinity.app.dashboardbottom.ui.fragment

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.projectinfinity.app.R
import com.projectinfinity.app.dashboardbottom.room.AppDatabase
import com.projectinfinity.app.dashboardbottom.room.Todo
import com.projectinfinity.app.utils.Constants.CAMERA_PERMISSION_REQUEST_CODE
import com.projectinfinity.app.utils.Constants.READ_EXTERNAL_STORAGE_REQUEST_CODE
import com.projectinfinity.app.utils.Constants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE
import com.projectinfinity.app.utils.GeneralUtils.databaseReference
import com.projectinfinity.app.utils.GeneralUtils.displayToast
import com.projectinfinity.app.utils.GeneralUtils.firebaseAuth
import com.projectinfinity.app.utils.GeneralUtils.getImageUri
import com.projectinfinity.app.utils.GeneralUtils.getLoader
import com.projectinfinity.app.utils.GeneralUtils.isConnectionAvailable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_to_do.*
import java.util.*

class ToDoFragment : Fragment(R.layout.fragment_to_do) {
    @SuppressLint("ResourceType")
    lateinit var captureImage: Bitmap
    var getUri: String = ""
    lateinit var bottomSheet: BottomSheetDialog
    lateinit var imageUri: Uri


    private val storageRef = FirebaseStorage.getInstance().reference
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val todoId = arguments?.getString("todoId")

        //If todoId is not null, data will update
        //Else new data create
        if (todoId != null) {
            //Calling updateTodo function
            updateTodo()
        } else {

            //If click of floating action button then new fragment will open for adding new
            iv_back_to_do_fragment.setOnClickListener {
                requireActivity().finish()

            }

            //On Click on Upload Image

            iv_upload_image.setOnClickListener {
                if(isConnectionAvailable(requireContext())) {
                    bottomSheet = BottomSheetDialog(requireContext())
                    bottomSheet.show()
                    uploadPermission()
                }
                else{
                    requireActivity().displayToast("Please On your Internet Connection")
                }

            }


            //If click on floating action button
            fab_to_do_fragment.setOnClickListener {

                //Validation checking
                if (isToDoDataValid(
                        et_task_name_fragment_todo.text.toString(),
                        et_todo_description_fragment_todo.text.toString(),
                        iv_profile_picture
                    )
                ) {
                    val todoId =
                        databaseReference().child("todo").child(firebaseAuth().uid!!).push().key
                    databaseReference().child("todo").child(firebaseAuth().uid!!)
                        .child(todoId!!)
                        .setValue(
                            Todo(
                                et_task_name_fragment_todo.text.toString(),
                                et_todo_description_fragment_todo.text.toString(),
                                false,
                                todoId,
                                getUri
                            )
                        )
                    AsyncTask.execute {
                        //Creating the  Data in room database
                        AppDatabase.getDatabase(requireContext()).todoDao().insertUsers(
                            Todo(
                                et_task_name_fragment_todo.text.toString(),
                                et_todo_description_fragment_todo.text.toString(),
                                false,
                                todoId, getUri
                            )
                        )
                    }
                    requireContext().displayToast("Todo Added")

                    //Edit text will clear after creation
                    et_task_name_fragment_todo.setText("")
                    et_todo_description_fragment_todo.setText("")
                    val profile: ImageView = iv_profile_picture_duplicate

                    profile.setImageDrawable(resources.getDrawable(R.drawable.profile_picture))
                    requireActivity().finish()

                }
            }
        }
    }

    //uploadPermission  will check permissions
    private fun uploadPermission() {
        bottomSheet.setContentView(R.layout.upload_bottom_sheet)
        val takePhoto: LinearLayout? = bottomSheet.findViewById(R.id.ll_take_a_photo)
        val gallery: LinearLayout? = bottomSheet.findViewById(R.id.ll_gallery)
        val cancel: LinearLayout? = bottomSheet.findViewById(R.id.ll_cancel)
        //if click on take a photo
        takePhoto?.setOnClickListener {
            bottomSheet.hide()

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                requireActivity().displayToast("take photo")
                //fun of taking photo from camera
                takePhoto()


            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        }
        //if click on gallery
        gallery?.setOnClickListener {
            bottomSheet.hide()

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {


                requireActivity().displayToast("select photo")
                //taking photo from gallery
                takeGalleryPhoto()
            } else {

                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
                    ),
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE
                )

            }

        }
        //if click on cancel
        cancel?.setOnClickListener {
            bottomSheet.hide()

            requireActivity().displayToast("cancel")
        }

    }

    //request Permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //if camera,read,write permission is allowed
        if ((requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            takePhoto()
        }
        //else if read,write permission is allowed
        else if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            takeGalleryPhoto()

        } else {
            requireActivity().displayToast("Permission Denied")
        }
    }

    //fun of taking gallery from photo
    fun takeGalleryPhoto() {
        //creating intent for selecting image from gallery
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(galleryIntent, "Select Picture"),
            READ_EXTERNAL_STORAGE_REQUEST_CODE
        )

    }

    //Function of updating singleTodo
    private fun updateTodo() {
        //Taking value from bundles key
        val todoId = arguments?.getString("todoId")
        val taskName = arguments?.getString("taskName")
        val taskDescription = arguments?.getString("taskDescription")
        val taskStatus = arguments?.getBoolean("taskStatus")
        val image = arguments?.getString("image")
        var status: Boolean = false

        //Sets text on top message
        tv_message_new_todo_fragment_todo.text = "Update Todo"
        et_task_name_fragment_todo.setText("$taskName")
        et_todo_description_fragment_todo.setText("$taskDescription")
        val profile: ImageView = iv_profile_picture_duplicate
        Picasso.get().load(image).into(profile)

        //if taskStatus is true
        if (taskStatus!!) {
            cb_completed_fragment_to_do.isChecked = true
        }
        //If click on back button
        iv_back_to_do_fragment.setOnClickListener {
            //recent activity will finish
            requireActivity().finish()
        }

        //On Click on Upload Image

        iv_upload_image.setOnClickListener {
            if(isConnectionAvailable(requireContext())) {
                bottomSheet = BottomSheetDialog(requireContext())
                bottomSheet.show()
                uploadPermission()
            }
            else{
                requireActivity().displayToast("Please On your Internet Connection")
            }

        }

        //On floating action button click
        fab_to_do_fragment.setOnClickListener {
            if (isToDoDataValid(
                    et_task_name_fragment_todo.text.toString(),
                    et_todo_description_fragment_todo.text.toString(),iv_profile_picture

                )
            ) {
                //If status is checked true will store in status variable
                //Else store false
                if (cb_completed_fragment_to_do.isChecked) {
                    status = true
                }

                //Updating the current Data from firebase
                databaseReference().child("todo").child(firebaseAuth().currentUser?.uid!!)
                    .child(todoId!!)
                    .setValue(
                        Todo(
                            et_task_name_fragment_todo.text.toString(),
                            et_todo_description_fragment_todo.text.toString(),
                            status,
                            todoId, image
                        )
                    )
                AsyncTask.execute {
                    //Updating the current Data from room database
                    AppDatabase.getDatabase(requireContext()).todoDao().update(
                        Todo(
                            et_task_name_fragment_todo.text.toString(),
                            et_todo_description_fragment_todo.text.toString(),
                            status,
                            todoId, image
                        )
                    )
                }
                requireContext().displayToast("Todo Updated")
                //Edit text will clear after update
                et_task_name_fragment_todo.setText("")
                et_todo_description_fragment_todo.setText("")
                val profile: ImageView = iv_profile_picture
                profile.setImageDrawable(resources.getDrawable(R.drawable.profile_picture))
                requireActivity().finish()
            }
        }
    }

    //Function of check validation
    private fun isToDoDataValid(name: String, description: String,image:ImageView): Boolean {
        if (name.isEmpty()) {
            //Showing toast
            requireActivity().displayToast(getString(R.string.Please_Enter_Task_Name))
            return false
        }  else if (description.isEmpty()) {
            //Showing toast
            requireActivity().displayToast(getString(R.string.Please_Enter_Task_Description))
            return false
        }else if (!image.isVisible) {
            //Showing toast
            requireActivity().displayToast(getString(R.string.Please_Enter_Image))
            return false
        }
        return true
    }

    //takePhoto
    fun takePhoto() {
        //creating intent for taking image from camera
        val takePhotoIntent =
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePhotoIntent, CAMERA_PERMISSION_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            captureImage = data.extras?.get("data") as Bitmap
            iv_profile_picture.setImageBitmap(captureImage)
            var imageUri: Uri? = getImageUri(requireActivity(), captureImage)
            uploadPicture(imageUri)
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            var imageUri: Uri? = data.data!!

            iv_profile_picture.setImageURI(imageUri)

            uploadPicture(imageUri)

        }

    }


    fun uploadPicture(imageUri: Uri?) {
        //if imageUri is not null
        if (imageUri != null) {

            var path = "image/" + UUID.randomUUID().toString()
            //create reference of storage
            val reference: StorageReference =
                storageRef.child(path)
            val uploadTask: UploadTask = reference.putFile(imageUri)
            //loader
            val loader = requireActivity().getLoader("Loader")
            loader.show()

            //if image will upload on firebase storage
            uploadTask.addOnSuccessListener {
                storageRef.child(path).downloadUrl.addOnSuccessListener {
                    loader.hide()
                    getUri = it.toString()
                }.addOnFailureListener {
                    requireActivity().displayToast(it.message + "")
                }
            }
        } else {
            requireActivity().displayToast("ImageUri is null")
        }


    }


}