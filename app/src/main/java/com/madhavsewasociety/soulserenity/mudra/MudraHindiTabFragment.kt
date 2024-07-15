package com.madhavsewasociety.soulserenity.mudra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.madhavsewasociety.soulserenity.databinding.FragmentMudraHindiTabBinding

class MudraHindiTabFragment : Fragment() {

    private lateinit var binding: FragmentMudraHindiTabBinding
    private lateinit var mudraActivity: MudraActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMudraHindiTabBinding.inflate(layoutInflater, container, false)
        mudraActivity=activity as MudraActivity

        binding.titleMudra.text=mudraActivity.mudraArrayList[mudraActivity.position].hindi.name.replace("\\n","\n")

        binding.note.text=mudraActivity.mudraArrayList[mudraActivity.position].hindi.note.replace("\\n","\n")

        binding.duration.text=mudraActivity.mudraArrayList[mudraActivity.position].hindi.duration.replace("\\n","\n")

        binding.steps.text=mudraActivity.mudraArrayList[mudraActivity.position].hindi.steps.replace("\\n","\n")

        binding.release.text=mudraActivity.mudraArrayList[mudraActivity.position].hindi.release.replace("\\n","\n")

        binding.definition.text=mudraActivity.mudraArrayList[mudraActivity.position].hindi.definition.replace("\\n","\n")

        binding.benfits.text=mudraActivity.mudraArrayList[mudraActivity.position].hindi.benefits.replace("\\n","\n")

        Glide.with(mudraActivity).load(mudraActivity.mudraArrayList[mudraActivity.position].hindi.imgUrl).into(binding.img)

        return binding.root
    }

}