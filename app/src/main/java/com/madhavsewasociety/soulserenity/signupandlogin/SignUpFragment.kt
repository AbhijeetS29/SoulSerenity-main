package com.madhavsewasociety.soulserenity.signupandlogin

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.madhavsewasociety.soulserenity.R
import com.madhavsewasociety.soulserenity.databinding.FragmentSignUpBinding
import com.madhavsewasociety.soulserenity.databinding.ProgressDialogBinding

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    private lateinit var signUpActivity: SignUpActivity
    private lateinit var auth: FirebaseAuth
    private lateinit var emailRef: DatabaseReference
    private lateinit var progressDialog: Dialog
    private lateinit var progressDialogBinding: ProgressDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        signUpActivity = activity as SignUpActivity
        auth = FirebaseAuth.getInstance()
        emailRef = FirebaseDatabase.getInstance().reference.child("email")

        progressDialog = Dialog(signUpActivity)
        progressDialogBinding = ProgressDialogBinding.inflate(layoutInflater)
        progressDialog.setContentView(progressDialogBinding.root)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setCancelable(false)

        binding.btnNext.setOnClickListener {
            if (binding.name.text.toString().isEmpty()) {
                binding.nameLayout.error = "Enter name"
                binding.name.requestFocus()
            } else if (binding.email.text.toString().isEmpty()) {
                binding.emailLayout.error = "Enter email"
                binding.email.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches()) {
                binding.emailLayout.error = "Enter valid email"
                binding.email.requestFocus()
            } else if (binding.password.text.toString().isEmpty()) {
                binding.passLayout.error = "Enter Password"
                binding.password.requestFocus()
            } else if (binding.password.text.toString().length < 6) {
                binding.passLayout.error = "Password must be of at least 6 characters"
                binding.password.requestFocus()
            } else if (binding.confirmPassword.text.toString().isEmpty()) {
                binding.confirmPassLayout.error = "Enter Password again"
                binding.confirmPassword.requestFocus()
            } else if (binding.password.text.toString() != binding.confirmPassword.text.toString()
            ) {
                binding.confirmPassword.requestFocus()
                binding.confirmPassLayout.error = "Password should be same"
            } else {
                progressDialog.show()
                emailRef.child(binding.email.text.toString().replace("@", "").replace(".", ""))
                    .get()
                    .addOnCompleteListener { emailCheck ->
                        auth.fetchSignInMethodsForEmail(binding.email.text.toString())
                            .addOnCompleteListener { signInTask ->
                                if (signInTask.isSuccessful) {
                                    val signInMethods = signInTask.result.signInMethods
                                    if (signInMethods != null && signInMethods.contains("password")) {
                                        progressDialog.dismiss()
                                        Toast.makeText(
                                            signUpActivity,
                                            "Email already exists",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    } else if (emailCheck.result.value.toString() == "google") {
                                        progressDialog.dismiss()
                                        Toast.makeText(
                                            signUpActivity,
                                            "Email already exists",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        progressDialog.dismiss()
                                        val bundle = Bundle()
                                        bundle.putString("name", binding.name.text.toString())
                                        bundle.putString("email", binding.email.text.toString())
                                        bundle.putString("pass", binding.password.text.toString())
                                        signUpActivity.navController.navigate(
                                            R.id.selectProfile,bundle

                                        )
                                    }
                                }
//                                else if (signInTask.exception?.localizedMessage.toString() ==) {
//                                    Toast.makeText(
//                                        signUpActivity,
//                                        signInTask.exception?.localizedMessage.toString(),
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
                                else {
                                    println(signInTask.exception?.localizedMessage.toString())
                                    Toast.makeText(
                                        signUpActivity,
                                        signInTask.exception?.localizedMessage.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
            }
        }
        return binding.root
    }
}