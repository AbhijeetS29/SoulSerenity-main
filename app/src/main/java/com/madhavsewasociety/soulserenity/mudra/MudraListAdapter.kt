package com.madhavsewasociety.soulserenity.mudra

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.madhavsewasociety.soulserenity.homescreen.HomeScreenActivity
import com.madhavsewasociety.soulserenity.databinding.ItemMudraBinding

class MudraListAdapter(private var homeScreenActivity: HomeScreenActivity, private var mudraArrayList: ArrayList<Languages>) : RecyclerView.Adapter<MudraListAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemMudraBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMudraBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.nameMudra.text=mudraArrayList[position].english.name
        holder.binding.benefits.text=mudraArrayList[position].english.benefits
        Glide.with(homeScreenActivity).load(mudraArrayList[position].english.imgUrl).into(holder.binding.imgView)
        holder.itemView.setOnClickListener {
            val intent=Intent(homeScreenActivity, MudraActivity::class.java)
            intent.putExtra("mudra",mudraArrayList)
            intent.putExtra("pos",position)
            homeScreenActivity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mudraArrayList.size
    }
}




