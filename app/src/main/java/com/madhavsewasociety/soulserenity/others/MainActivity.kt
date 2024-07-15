package com.madhavsewasociety.soulserenity.others

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.madhavsewasociety.soulserenity.databinding.ActivityMainBinding
import com.madhavsewasociety.soulserenity.homescreen.HomeScreenActivity
import com.madhavsewasociety.soulserenity.signupandlogin.LoginActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
//    var REQUEST_PERMISSION_CODE = 1
//    var REQUEST_VIDEO_CODE = 2
//    var uri: Uri? = null
//    lateinit var storageRef: StorageReference
//    lateinit var databaseRef: DatabaseReference

//    private fun uploadVideoToFirebase(videoUri: Uri) {
//        storageRef.child(binding.et.text.toString()).putFile(videoUri)
//            .addOnCompleteListener { taskSnapshot ->
//                storageRef.child(binding.et.text.toString()).downloadUrl.addOnCompleteListener { downloadUrlTask ->
//                    println("aagya")
//                    if (downloadUrlTask.isSuccessful) {
//                        val imgUrl = downloadUrlTask.result.toString()
//                        databaseRef.child(binding.et.text.toString()).child("english")
//                            .child("imgUrl").setValue(imgUrl)
//                        databaseRef.child(binding.et.text.toString()).child("hindi").child("imgUrl")
//                            .setValue(imgUrl)
//                        databaseRef.child(binding.et.text.toString()).child("punjabi")
//                            .child("imgUrl").setValue(imgUrl)
//                    }
//                }
//            }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_VIDEO_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            uri = data.data ?: return
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        databaseRef = FirebaseDatabase.getInstance().reference.child("mudra")
//        storageRef = FirebaseStorage.getInstance().reference.child("mudra")
//        binding.btn.setOnClickListener {
//            var intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, REQUEST_VIDEO_CODE)
//        }
//
//        binding.submit.setOnClickListener {
//            uploadVideoToFirebase(uri!!)
//        }

        val handler=Handler()
        handler.postDelayed({
        val intent=Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
        },3100)
//        var mudra=Mudra()
//        FirebaseDatabase.getInstance().reference.child("mudra").child("akashMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("akashMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("akashMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("garudaMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("garudaMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("garudaMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("gyanMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("gyanMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("gyanMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("buddhiMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("buddhiMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("buddhiMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("anjaliMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("anjaliMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("anjaliMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("prithviMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("prithviMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("prithviMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("hridayaMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("hridayaMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("hridayaMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("shunyaMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("shunyaMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("shunyaMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("kuberMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("kuberMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("kuberMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("suryaMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("suryaMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("suryaMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("varunMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("varunMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("varunMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("vayuMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("vayuMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("vayuMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("brahmaMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("brahmaMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("brahmaMudra").child("punjabi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("pranaMudra").child("english").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("pranaMudra").child("hindi").setValue(mudra)
//        FirebaseDatabase.getInstance().reference.child("mudra").child("pranaMudra").child("punjabi").setValue(mudra)

    }
}