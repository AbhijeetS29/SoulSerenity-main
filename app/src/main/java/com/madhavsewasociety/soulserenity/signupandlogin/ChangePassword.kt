package com.madhavsewasociety.soulserenity.signupandlogin

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.madhavsewasociety.soulserenity.databinding.ActivityChangePasswordBinding
import com.madhavsewasociety.soulserenity.databinding.ProgressDialogBinding

class ChangePassword : AppCompatActivity() {
    lateinit var binding: ActivityChangePasswordBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog : Dialog
    private lateinit var progressDialogBinding: ProgressDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        progressDialog = Dialog(this@ChangePassword)
        progressDialogBinding= ProgressDialogBinding.inflate(layoutInflater)
        progressDialog.setContentView(progressDialogBinding.root)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setCancelable(false)

        binding.btnNext.setOnClickListener {
            if (binding.email.text.toString().isEmpty()) {
                binding.emailLayout.error = "Enter Email"
                binding.email.requestFocus()
            } else {
                progressDialog.show()
                auth.sendPasswordResetEmail(binding.email.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Reset Password link sent on your email!", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    }
                    else if(it.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.")
                    {
                        progressDialog.dismiss()
                        Toast.makeText(this,"Email does not exists", Toast.LENGTH_LONG)
                            .show()
                    }
                    else if(it.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.")
                    {
                        progressDialog.dismiss()
                        Toast.makeText(this,"Not a valid email", Toast.LENGTH_LONG)
                            .show()
                    }
                    else {
                        progressDialog.dismiss()
                        println(it.exception.toString())
                        Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }
}