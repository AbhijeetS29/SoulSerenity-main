package com.madhavsewasociety.soulserenity.signupandlogin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.madhavsewasociety.soulserenity.R
import com.madhavsewasociety.soulserenity.databinding.FragmentOtpBinding
import papaya.`in`.sendmail.SendMail
import kotlin.math.sign
import kotlin.random.Random
import kotlin.random.nextInt

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OtpFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentOtpBinding
    private lateinit var signUpActivity: SignUpActivity
    private var email: String = ""
    private var pass: String = ""
    private var name: String = ""
    private var random : Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(layoutInflater, container, false)
        signUpActivity = activity as SignUpActivity
        arguments.let { bundle ->
            email = bundle?.getString ("email").toString()
            name = bundle?.getString("name").toString()
            pass = bundle?.getString("pass").toString()
        }
        random()

        binding.tvEmail.setText(email.toString())

        binding.resendOtp.setOnClickListener {
            Toast.makeText(signUpActivity, "OTP has been sent again.", Toast.LENGTH_SHORT).show()
            random()
        }

        binding.otp1.doOnTextChanged { text, start, before, count ->
            if (!binding.otp1.text.toString().isEmpty()) {
                binding.otp2.requestFocus()
            }
            if (!binding.otp2.text.toString().isEmpty()) {
                binding.otp2.requestFocus()
            }
        }

        binding.otp2.doOnTextChanged { text, start, before, count ->
            if (!binding.otp2.text.toString().isEmpty()) {
                binding.otp3.requestFocus()
            } else {
                binding.otp1.requestFocus()
            }

        }
        binding.otp3.doOnTextChanged { text, start, before, count ->
            if (!binding.otp3.text.toString().isEmpty()) {
                binding.otp4.requestFocus()
            } else {
                binding.otp2.requestFocus()
            }

        }
        binding.otp4.doOnTextChanged { text, start, before, count ->
            if (!binding.otp4.text.toString().isEmpty()) {
                binding.otp5.requestFocus()
            } else {
                binding.otp3.requestFocus()
            }

        }
        binding.otp5.doOnTextChanged { text, start, before, count ->
            if (!binding.otp5.text.toString().isEmpty()) {
                binding.otp6.requestFocus()
            } else {
                binding.otp4.requestFocus()
            }

        }
        binding.otp6.doOnTextChanged { text, start, before, count ->
            if (binding.otp6.text.toString().isEmpty()) {
                binding.otp5.requestFocus()
            }

            binding.btnVerify.setOnClickListener {
                val otp1 = binding.otp1.text.toString()
                val otp2 = binding.otp2.text.toString()
                val otp3 = binding.otp3.text.toString()
                val otp4 = binding.otp4.text.toString()
                val otp5 = binding.otp5.text.toString()
                val otp6 = binding.otp6.text.toString()
                val otp = "$otp1$otp2$otp3$otp4$otp5$otp6"

                if (binding.otp1.text.toString().isEmpty() ||
                    binding.otp2.text.toString().isEmpty() ||
                    binding.otp3.text.toString().isEmpty() ||
                    binding.otp4.text.toString().isEmpty() ||
                    binding.otp5.text.toString().isEmpty() ||
                    binding.otp6.text.toString().isEmpty()
                ) {
                    Toast.makeText(signUpActivity, "Enter OTP", Toast.LENGTH_SHORT).show()
                } else if (!otp.equals(random.toString())) {
                    Toast.makeText(signUpActivity, "Wrong OTP", Toast.LENGTH_SHORT).show()
                } else {
                    val bundle=Bundle()
                    bundle.putString("name",name)
                    bundle.putString("email",email)
                    bundle.putString("pass",pass)
                    signUpActivity.navController.navigate(R.id.selectProfile,bundle)
                }

            }

        }
        return binding.root

    }

    fun random() {
        random = Random.nextInt(100000..999999)
        var mail = SendMail(
            "droidbytes11@gmail.com", "ojjzedbyawubvwlv", email, "Login Signup app's OTP",
            "Your OTP is -> $random"
        )
        mail.execute()
    }

}