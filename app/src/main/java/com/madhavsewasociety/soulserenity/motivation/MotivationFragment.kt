package com.madhavsewasociety.soulserenity.motivation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.madhavsewasociety.soulserenity.databinding.FragmentMotivationBinding
import com.madhavsewasociety.soulserenity.homescreen.HomeScreenActivity
class MotivationFragment : Fragment() {
    private lateinit var binding : FragmentMotivationBinding
    private lateinit var homeScreenActivity: HomeScreenActivity
    private lateinit var motivationList: ArrayList<Motivation>
    private lateinit var motivationAdapter: MotivationAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentMotivationBinding.inflate(layoutInflater,container,false)
        homeScreenActivity=activity as HomeScreenActivity
        databaseReference= FirebaseDatabase.getInstance().reference.child("motivation")
        motivationList=ArrayList()

        binding.shimmer.startShimmer()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                motivationList.clear()
                for(each in snapshot.children) {
                    val motivation = each.getValue(Motivation::class.java)
                    if (motivation != null) {
                        motivationList.add(motivation)
                    }
                    println(motivationList)
                }
                motivationList.reverse()
                motivationAdapter= MotivationAdapter(homeScreenActivity,motivationList)
                binding.recyclerView.layoutManager= LinearLayoutManager(homeScreenActivity)
                binding.recyclerView.adapter=motivationAdapter

                binding.shimmer.visibility = View.GONE
                binding.shimmer.stopShimmer()
                binding.recyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return binding.root
    }

}