package com.madhavsewasociety.soulserenity.signupandlogin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.madhavsewasociety.soulserenity.R
import com.madhavsewasociety.soulserenity.databinding.ActivityLoginBinding
import com.madhavsewasociety.soulserenity.databinding.ProgressDialogBinding
import com.madhavsewasociety.soulserenity.homescreen.HomeScreenActivity


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var userRef: DatabaseReference
    private lateinit var emailsRef: DatabaseReference
    private lateinit var progressDialog: Dialog
    private lateinit var progressDialogBinding: ProgressDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        userRef = FirebaseDatabase.getInstance().reference.child("users")
        emailsRef = FirebaseDatabase.getInstance().reference.child("email")

        progressDialog = Dialog(this@LoginActivity)
        progressDialogBinding = ProgressDialogBinding.inflate(layoutInflater)
        progressDialog.setContentView(progressDialogBinding.root)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setCancelable(false)

        binding.btnLogin.setOnClickListener {
            if (binding.email.text.toString().isEmpty()) {
                binding.emailLayout.error = "Enter email"
                binding.email.requestFocus()
            } else if (binding.password.text.toString().isEmpty()) {
                binding.passLayout.error = "Enter Password"
                binding.password.requestFocus()
            } else {
                progressDialog.show()
                progressDialog.setCancelable(false)

                auth.signInWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            progressDialog.dismiss()
                            val intent = Intent(this@LoginActivity, HomeScreenActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else if (it.exception?.localizedMessage.toString() == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
                        ) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Check your internet connection please",
                                Toast.LENGTH_SHORT
                            ).show()

                            progressDialog.dismiss()

                        } else if (it.exception?.localizedMessage.toString() == "There is no user record corresponding to this identifier. The user may have been deleted."
                        ) {
                            Toast.makeText(
                                this@LoginActivity,
                                "User does not exists or Wrong email Id",
                                Toast.LENGTH_SHORT
                            ).show()
                            progressDialog.dismiss()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                this@LoginActivity,
                                it.exception?.localizedMessage.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            println("login button -> ${it.exception?.localizedMessage.toString()}")
                        }
                    }
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.google.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleIntentLauncher.launch(signInIntent)
        }

        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ChangePassword::class.java)
            startActivity(intent)
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }


    }


    private val googleIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                googleHandleResults(task)
            }
        }

    private fun googleHandleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            progressDialog.show()
            val account: GoogleSignInAccount? = task.result
            emailsRef.child(
                account?.email.toString()
                    .replace("@", "").replace(".", "")
            ).get()
                .addOnCompleteListener { emailCheck ->
                    if (emailCheck.result.value.toString() == "email") {
                        progressDialog.dismiss()
                        googleSignInClient.signOut()
                        Toast.makeText(
                            this@LoginActivity,
                            "Email already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        googleUpdateUI(account!!)
                    }
                }
        }
    }

    private fun googleUpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                var users = Users("", "", "", "")
                userRef.child(auth.currentUser?.uid.toString()).child("email").get()
                    .addOnCompleteListener { getProfile ->
                        if (getProfile.isSuccessful) {
                            if (getProfile.result.value == null) {
                                users = Users(
                                    account.displayName.toString(),
                                    auth.currentUser?.uid.toString(),
                                    account.email.toString(),
                                    account.photoUrl.toString()
                                )
                                uploadToFirebase(users, account)
                            } else {
                                val intent =
                                    Intent(this@LoginActivity, HomeScreenActivity::class.java)
                                intent.putExtra("email", account.email)
                                intent.putExtra("name", account.displayName)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
            } else {
                progressDialog.dismiss()
                Toast.makeText(this, it.exception?.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadToFirebase(users: Users, account: GoogleSignInAccount) {
        userRef.child(auth.currentUser?.uid.toString()).setValue(users)
            .addOnCompleteListener {
                var email = account.email.toString().replace("@", "").replace(".", "")
                emailsRef.child(email).setValue("google").addOnCompleteListener { emailUpload ->
                    if (emailUpload.isSuccessful) {
                        progressDialog.dismiss()
                        val intent = Intent(this@LoginActivity, HomeScreenActivity::class.java)
                        intent.putExtra("email", account.email)
                        intent.putExtra("name", account.displayName)
                        startActivity(intent)
                        finish()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@LoginActivity,
                            emailUpload.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser?.uid
        if (user != null) {
            val intent = Intent(this@LoginActivity, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
            progressDialog.dismiss()
    }

}
