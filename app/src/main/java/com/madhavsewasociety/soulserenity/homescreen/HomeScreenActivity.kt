package com.madhavsewasociety.soulserenity.homescreen

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.madhavsewasociety.soulserenity.R
import com.madhavsewasociety.soulserenity.databinding.ActivityHomeScreenBinding
import com.madhavsewasociety.soulserenity.databinding.ChangeProfileDialogBinding
import com.madhavsewasociety.soulserenity.databinding.ProgressDialogBinding
import com.madhavsewasociety.soulserenity.signupandlogin.ChangePassword
import com.madhavsewasociety.soulserenity.signupandlogin.LoginActivity
import java.io.ByteArrayOutputStream

class HomeScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeScreenBinding
    lateinit var navController: NavController
    private lateinit var userRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val PICK_IMAGE_REQUEST_CODE = 1
    val CROP_IMAGE_REQUEST_CODE = 2
    private var croppedImage: Bitmap? = null
    private lateinit var headerProfile: ImageView
    private lateinit var changeProfileDialog: Dialog
    private lateinit var changeProfileDialogBinding: ChangeProfileDialogBinding
    lateinit var progressDialog: Dialog
    private lateinit var progressDialogBinding: ProgressDialogBinding
    private lateinit var storageRef: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.navController)
        auth = FirebaseAuth.getInstance()
        userRef = FirebaseDatabase.getInstance().reference.child("users")
        storageRef = FirebaseStorage.getInstance().reference.child("usersProfile")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this@HomeScreenActivity, gso)

        progressDialog = Dialog(this@HomeScreenActivity)
        progressDialogBinding = ProgressDialogBinding.inflate(layoutInflater)
        progressDialog.setContentView(progressDialogBinding.root)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setCancelable(false)

        changeProfileDialog = Dialog(this@HomeScreenActivity)
        changeProfileDialogBinding = ChangeProfileDialogBinding.inflate(layoutInflater)
        changeProfileDialog.setContentView(changeProfileDialogBinding.root)
        changeProfileDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val headerView = binding.sideBar.getHeaderView(0)
        headerProfile = headerView.findViewById(R.id.profile)
        val headerName = headerView.findViewById<TextView>(R.id.name)
        val headerEmail = headerView.findViewById<TextView>(R.id.email)

        userRef.child(auth.currentUser?.uid.toString()).child("photoUrl").get()
            .addOnCompleteListener {
                Glide.with(this@HomeScreenActivity).load(it.result.value.toString())
                    .into(headerProfile)
                Glide.with(this@HomeScreenActivity).load(it.result.value.toString())
                    .into(changeProfileDialogBinding.profile)
            }

        userRef.child(auth.currentUser?.uid.toString()).child("userName").get()
            .addOnCompleteListener {
                headerName.text = it.result.value.toString()
            }

        userRef.child(auth.currentUser?.uid.toString()).child("email").get()
            .addOnCompleteListener {
                headerEmail.text = it.result.value.toString()
            }

        binding.apply {
            val toggle = ActionBarDrawerToggle(
                this@HomeScreenActivity,
                drawerLayout,
                R.string.open,
                R.string.close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            headerProfile.setOnClickListener {
                changeProfileDialog.show()
            }

            changeProfileDialogBinding.profile.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this@HomeScreenActivity,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@HomeScreenActivity,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        PICK_IMAGE_REQUEST_CODE
                    )
                } else {
                    pickImageFromGallery()
                }
            }

            sideBar.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.logout -> {
                        val alertDialog = AlertDialog.Builder(this@HomeScreenActivity)
                        alertDialog.setTitle("Are you sure you want to Logout? ")
                        alertDialog.setPositiveButton("Yes") { dialog, _ ->
                            dialog.dismiss()
                            drawerLayout.close()
                            auth.signOut()
                            googleSignInClient.signOut()
                            val intent = Intent(this@HomeScreenActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        alertDialog.setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }

                        alertDialog.show()
                    }

                    R.id.changePassword -> {
                        val intent = Intent(this@HomeScreenActivity, ChangePassword::class.java)
                        startActivity(intent)
                    }

                    R.id.reportBug -> {
                        drawerLayout.close()
                        val builder = AlertDialog.Builder(this@HomeScreenActivity)
                        builder.setTitle("Report a Bug")
                        builder.setMessage(
                            "Dear valued user, we hope you're enjoying our Android app. " +
                                    "We sincerely appreciate your taking the time to let us know if you run into " +
                                    "any bugs or problems by sending an email to us."
                        )

                        builder.setPositiveButton("Report") { dialog, _ ->
                            val email = arrayOf("s2.abhijeet@gmail.com","sjapnoor11@gmail.com")
                            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:") // Only email apps should handle this
                                putExtra(Intent.EXTRA_EMAIL, email)
                                putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                                putExtra(Intent.EXTRA_TEXT, "Body Here")
                            }
                            startActivity(emailIntent)
                            dialog.dismiss()
                        }


                        builder.setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }

                        builder.show()
                    }

                    R.id.emailVivek -> {
                        drawerLayout.close()
                        val email = "vivek.joshi1980@gmail.com"
                        val emailIntent = Intent(Intent.ACTION_SENDTO)
                        emailIntent.data = Uri.parse("mailto:$email")
                        startActivity(emailIntent)
                    }

                    R.id.youtubeVivek -> {
                        drawerLayout.close()
                        val linkUrl = "https://www.youtube.com/@vivek708"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                        startActivity(intent)
                    }

                    R.id.instagramvivek -> {
                        drawerLayout.close()
                        val linkUrl = "https://www.instagram.com/vivekjoshimasoom/"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                        startActivity(intent)
                    }

                    R.id.facebookVivek -> {
                        drawerLayout.close()
                        val linkUrl = "https://www.facebook.com/vivek.joshi1980"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                        startActivity(intent)
                    }

                    R.id.emailDeveloper -> {
                        val email = arrayOf("sjapnoor11@gmail.com", "s2.abhijeet@gmail.com")
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:") // Only email apps should handle this
                            putExtra(Intent.EXTRA_EMAIL, email)
                            putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                            putExtra(Intent.EXTRA_TEXT, "Body Here")
                        }
                        startActivity(emailIntent)
                    }

                    R.id.instagramDeveloper -> {
                        drawerLayout.close()
                        val linkUrl = "https://www.instagram.com/abhijeet.singh09/"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                        startActivity(intent)
                    }
                }
                true
            }
        }
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
            progressDialog.show()
            val imagesRef = storageRef.child(auth.currentUser?.uid.toString())
            val bao = ByteArrayOutputStream()
            croppedImage?.compress(Bitmap.CompressFormat.JPEG, 100, bao)
            val uploadTask = imagesRef.putBytes(bao.toByteArray())
            uploadTask.addOnCompleteListener {
                if (it.isSuccessful) {
                    imagesRef.downloadUrl.addOnSuccessListener { url ->
                        uploadDataToFirebase(url.toString())
                        progressDialog.dismiss()
                        changeProfileDialog.dismiss()
                        Glide.with(this@HomeScreenActivity).load(url).into(headerProfile)
                        Glide.with(this@HomeScreenActivity).load(url)
                            .into(changeProfileDialogBinding.profile)
                        Glide.with(this@HomeScreenActivity).load(url).into(headerProfile)
                        navController.navigate(R.id.homeScreenFragment)
                    }
                } else if (it.exception?.localizedMessage.toString() == "A network error (such as timeout, i" +
                    "nterrupted connection " +
                    "or unreachable host) " +
                    "has occurred."
                ) {
                    Toast.makeText(
                        this@HomeScreenActivity,
                        "Check your internet connection please",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@HomeScreenActivity,
                        it.exception?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun uploadDataToFirebase(photoUrl: String) {
        userRef.child(auth.currentUser?.uid.toString()).child("photoUrl").setValue(photoUrl)
    }

}