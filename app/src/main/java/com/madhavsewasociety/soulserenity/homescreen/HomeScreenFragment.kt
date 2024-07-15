package com.madhavsewasociety.soulserenity.homescreen

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.madhavsewasociety.soulserenity.R
import com.madhavsewasociety.soulserenity.databinding.ContactUsDialogBinding
import com.madhavsewasociety.soulserenity.databinding.FragmentHomeScreenBinding
import com.madhavsewasociety.soulserenity.others.VideoActivity
import java.util.Calendar

class HomeScreenFragment : Fragment() {
    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var motivationRef: DatabaseReference
    private lateinit var homeScreenActivity: HomeScreenActivity
    private lateinit var userRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater, container, false)
        homeScreenActivity = activity as HomeScreenActivity
        auth = FirebaseAuth.getInstance()
        motivationRef = FirebaseDatabase.getInstance().reference.child("motivation")
        userRef = FirebaseDatabase.getInstance().reference.child("users")

        binding.profile.setOnClickListener {
            homeScreenActivity.binding.drawerLayout.open()
        }

        userRef.child(auth.currentUser?.uid.toString()).child("userName").get()
            .addOnCompleteListener {
                binding.name.text = it.result.value.toString()
            }

        userRef.child(auth.currentUser?.uid.toString()).child("photoUrl").get()
            .addOnCompleteListener {
                Glide.with(homeScreenActivity).load(it.result.value.toString())
                    .into(binding.profile)
            }


        val calendar = Calendar.getInstance()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val currentDate = "$dayOfMonth-$month-$year"

        motivationRef.child(currentDate).child("title").get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result.value == null) {
                    binding.dailyMotivationTv.text = "Daily Dose of Motivation\n(Not yet uploaded)"
                } else {
                    binding.dailyMotivationTv.text = it.result.value.toString()
                }
            } else if (it.exception?.localizedMessage.toString() == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
            ) {
                Toast.makeText(
                    homeScreenActivity,
                    "Check your internet connection please",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    homeScreenActivity,
                    it.exception?.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.dailyMotivation.setOnClickListener {
            homeScreenActivity.progressDialog.show()
            motivationRef.child(currentDate).child("videourl").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.value == null) {
                        homeScreenActivity.progressDialog.dismiss()
                        Toast.makeText(homeScreenActivity, "Not uploaded yet", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        homeScreenActivity.progressDialog.dismiss()
                        val intent = Intent(homeScreenActivity, VideoActivity::class.java)
                        intent.putExtra("url", it.result.value.toString())
                        homeScreenActivity.startActivity(intent)
                    }
                } else if (it.exception?.localizedMessage.toString() == "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
                ) {
                    Toast.makeText(
                        homeScreenActivity,
                        "Check your internet connection please",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    homeScreenActivity.progressDialog.dismiss()
                    Toast.makeText(
                        homeScreenActivity,
                        it.exception?.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.mudra.setOnClickListener {
            homeScreenActivity.navController.navigate(R.id.mudraListFragment)
        }

        binding.motivation.setOnClickListener {
            homeScreenActivity.navController.navigate(R.id.motivationFragment)
        }

        binding.meditation.setOnClickListener {
            homeScreenActivity.navController.navigate(R.id.meditationFragment)
        }
        binding.aboutUs.setOnClickListener {
            val intent=Intent(homeScreenActivity,AboutUsActivity::class.java)
            startActivity(intent)
        }
        binding.contactUS.setOnClickListener {
            val dialog = Dialog(homeScreenActivity)
            val dialogBinding = ContactUsDialogBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            dialogBinding.em.setOnClickListener {
                val email = "vivek.joshi1980@gmail.com"
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse("mailto:$email")
                startActivity(emailIntent)
            }

            dialogBinding.ph.setOnClickListener {
                val phoneNumber="7009147526"
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")
                startActivity(intent)
            }

            dialogBinding.ig.setOnClickListener {
                val linkUrl = "https://www.instagram.com/vivekjoshimasoom/"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                startActivity(intent)
            }

            dialogBinding.yt.setOnClickListener {
                val linkUrl = "https://www.youtube.com/@vivek708"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                startActivity(intent)
            }

            dialogBinding.fb.setOnClickListener {
                val linkUrl = "https://www.facebook.com/vivek.joshi1980"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                startActivity(intent)
            }



        }

        return binding.root
    }

}