package com.madhavsewasociety.soulserenity.mudra

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
import com.madhavsewasociety.soulserenity.databinding.FragmentMudraListBinding

class MudraListFragment : Fragment() {
    lateinit var binding : FragmentMudraListBinding
    lateinit var homeScreenActivity: HomeScreenActivity
    private lateinit var mudraEngRef : DatabaseReference
    private lateinit var mudraHindiRef : DatabaseReference
    private lateinit var mudraPunRef : DatabaseReference
    private lateinit var mudraArrayList : ArrayList<Languages>
    private lateinit var mudraListAdapter: MudraListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentMudraListBinding.inflate(layoutInflater, container, false)
        homeScreenActivity=activity as HomeScreenActivity
        mudraArrayList= ArrayList()
        mudraEngRef= FirebaseDatabase.getInstance().reference.child("mudra")
        mudraHindiRef= FirebaseDatabase.getInstance().reference.child("mudra")
        mudraPunRef= FirebaseDatabase.getInstance().reference.child("mudra")

        binding.shimmer.startShimmer()

        mudraEngRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mudraArrayList.clear()
                for(each in snapshot.children){
                    val mudra=each.getValue(Languages::class.java)
                    if(mudra !=null){
                        mudraArrayList.add(mudra)
                    }
                }
                mudraArrayList.reverse()
                mudraListAdapter= MudraListAdapter(homeScreenActivity, mudraArrayList)
                binding.recyclerView.layoutManager= LinearLayoutManager(homeScreenActivity)
                binding.recyclerView.adapter=mudraListAdapter

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