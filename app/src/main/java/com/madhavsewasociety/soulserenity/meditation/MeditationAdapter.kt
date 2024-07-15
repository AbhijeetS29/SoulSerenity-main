package com.madhavsewasociety.soulserenity.meditation

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.madhavsewasociety.soulserenity.homescreen.HomeScreenActivity
import com.madhavsewasociety.soulserenity.databinding.ItemMeditationBinding

class MeditationAdapter(
    private var homeScreenActivity: HomeScreenActivity,
    private var meditationList: ArrayList<Meditation>
) : RecyclerView.Adapter<MeditationAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemMeditationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMeditationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = meditationList[position].title
        holder.binding.link.text = meditationList[position].link
        Glide.with(homeScreenActivity).load(meditationList[position].imgUrl)
            .into(holder.binding.imgView)

        holder.itemView.setOnClickListener {
            val videoUrl = meditationList[position].link
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            homeScreenActivity.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {

            true
        }

    }

    override fun getItemCount(): Int {
        return meditationList.size
    }
}




