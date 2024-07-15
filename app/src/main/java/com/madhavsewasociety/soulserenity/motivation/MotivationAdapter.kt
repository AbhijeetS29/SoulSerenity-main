package com.madhavsewasociety.soulserenity.motivation

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.madhavsewasociety.soulserenity.others.VideoActivity
import com.madhavsewasociety.soulserenity.databinding.ItemMotivationBinding
import com.madhavsewasociety.soulserenity.homescreen.HomeScreenActivity

class MotivationAdapter(private var motivationActivity: HomeScreenActivity, private var motivationList : ArrayList<Motivation>) : RecyclerView.Adapter<MotivationAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemMotivationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMotivationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text=motivationList[position].title
        holder.binding.date.text= motivationList[position].date
        holder.itemView.setOnClickListener {
            val intent= Intent(motivationActivity, VideoActivity::class.java)
            intent.putExtra("url",motivationList[position].videourl)
            println("Url" + motivationList[position].videourl)
            motivationActivity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return motivationList.size
    }
}




