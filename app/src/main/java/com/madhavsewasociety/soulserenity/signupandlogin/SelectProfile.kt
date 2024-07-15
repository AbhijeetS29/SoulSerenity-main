package com.madhavsewasociety.soulserenity.signupandlogin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.madhavsewasociety.soulserenity.databinding.FragmentSelectProfileBinding
import com.madhavsewasociety.soulserenity.databinding.ProgressDialogBinding
import com.madhavsewasociety.soulserenity.homescreen.HomeScreenActivity
import java.io.ByteArrayOutputStream


class SelectProfile : Fragment() {
    lateinit var binding: FragmentSelectProfileBinding
    private lateinit var signUpActivity: SignUpActivity
    private var email: String = ""
    private var name: String = ""
    private var pass: String = ""
    private val PICK_IMAGE_REQUEST_CODE = 1
    private val CROP_IMAGE_REQUEST_CODE = 2
    private var croppedImage: Bitmap? = null
    private var profileCheck: Boolean = false
    private lateinit var auth: FirebaseAuth
    private lateinit var userRef: DatabaseReference
    private lateinit var emailRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var progressDialog : Dialog
    private lateinit var progressDialogBinding: ProgressDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectProfileBinding.inflate(layoutInflater, container, false)
        signUpActivity = activity as SignUpActivity
        auth = FirebaseAuth.getInstance()
        userRef = FirebaseDatabase.getInstance().reference.child("users")
        emailRef = FirebaseDatabase.getInstance().reference.child("email")
        storageRef = FirebaseStorage.getInstance().reference.child("usersProfile")

        arguments.let { bundle ->
            email = bundle?.getString("email").toString()
            name = bundle?.getString("name").toString()
            pass = bundle?.getString("pass").toString()
        }

        progressDialog= Dialog(signUpActivity)
        progressDialogBinding= ProgressDialogBinding.inflate(layoutInflater)
        progressDialog.setContentView(progressDialogBinding.root)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.profile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    signUpActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    signUpActivity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PICK_IMAGE_REQUEST_CODE
                )
            } else {
                pickImageFromGallery()
            }
        }

        binding.btnNext.setOnClickListener {
            progressDialog.show()
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { createUser ->
                if (createUser.isSuccessful) {
                    if (!profileCheck) {
                        uploadDataToFirebase("")
                    } else {
                        val imagesRef = storageRef.child(auth.currentUser?.uid.toString())
                        val bao = ByteArrayOutputStream()
                        croppedImage?.compress(Bitmap.CompressFormat.JPEG, 100, bao)
                        val data = bao.toByteArray()
                        val uploadTask = imagesRef.putBytes(data)
                        uploadTask.addOnCompleteListener {
                            if (it.isSuccessful) {
                                imagesRef.downloadUrl.addOnSuccessListener {url->
                                    uploadDataToFirebase(url.toString())
                                }
                            } else {
                                println("profileUpload -> ${it.exception.toString()}")
                                progressDialog.dismiss()
                                Toast.makeText(
                                    signUpActivity,
                                    it.exception?.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                else if(createUser.exception.toString()=="com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred."){
                    progressDialog.dismiss()
                    Toast.makeText(signUpActivity, "Check your internet connection please", Toast.LENGTH_SHORT).show()
                }
                else{
                    progressDialog.dismiss()
                    println(createUser.exception.toString())
                    Toast.makeText(signUpActivity, createUser.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }
        return binding.root
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            // Crop image using built-in Android crop intent
            val cropIntent = Intent("com.android.camera.action.CROP")
            cropIntent.setDataAndType(selectedImageUri, "image/*")
            cropIntent.putExtra("crop", "true")
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            cropIntent.putExtra("outputX", 300)
            cropIntent.putExtra("outputY", 300)
            cropIntent.putExtra("return-data", true)
            startActivityForResult(cropIntent, CROP_IMAGE_REQUEST_CODE)
        } else if (requestCode == CROP_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Display cropped image using Glide library
            croppedImage = data.extras?.getParcelable("data")
            Glide.with(this).load(croppedImage).into(binding.profile)
            profileCheck = true
        }
    }

    private fun uploadDataToFirebase(photoUrl: String) {
        val users = Users(name, auth.currentUser?.uid.toString(), email, photoUrl)
        userRef.child(auth.currentUser?.uid.toString()).setValue(users).addOnCompleteListener {
            emailRef.child(email.replace("@", "").replace(".", "")).setValue("email")
            progressDialog.dismiss()
            val intent = Intent(signUpActivity, HomeScreenActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("name", name)
            signUpActivity.startActivity(intent)
            signUpActivity.finish()
        }
    }

}