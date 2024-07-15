package com.madhavsewasociety.soulserenity.meditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.madhavsewasociety.soulserenity.homescreen.HomeScreenActivity
import com.madhavsewasociety.soulserenity.databinding.FragmentMeditationBinding

class MeditationFragment : Fragment() {
    private lateinit var binding : FragmentMeditationBinding
    private lateinit var meditationRef : DatabaseReference
    private lateinit var meditationArrayList : ArrayList<Meditation>
    private lateinit var meditationAdapter : MeditationAdapter
    private lateinit var homeScreenActivity : HomeScreenActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentMeditationBinding.inflate(layoutInflater, container, false)
        homeScreenActivity=activity as HomeScreenActivity
        meditationArrayList= ArrayList()
        meditationRef= FirebaseDatabase.getInstance().reference.child("meditation")

        binding.shimmer.startShimmer()

        meditationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                meditationArrayList.clear()
                for(each in snapshot.children){
                    val meditation=each.getValue(Meditation::class.java)
                    if(meditation!=null){
                        meditationArrayList.add(meditation)
                    }
                }
                meditationArrayList.reverse()
                meditationAdapter= MeditationAdapter(homeScreenActivity, meditationArrayList)
                binding.recyclerView.layoutManager= LinearLayoutManager(homeScreenActivity)
                binding.recyclerView.adapter=meditationAdapter

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