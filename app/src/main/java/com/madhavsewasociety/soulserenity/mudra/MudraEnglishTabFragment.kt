package com.madhavsewasociety.soulserenity.mudra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.madhavsewasociety.soulserenity.databinding.FragmentMudraEnglishTabBinding

class MudraEnglishTabFragment : Fragment() {
    private lateinit var binding: FragmentMudraEnglishTabBinding
    private lateinit var mudraActivity: MudraActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMudraEnglishTabBinding.inflate(layoutInflater, container, false)
        mudraActivity=activity as MudraActivity

        binding.titleMudra.text=mudraActivity.mudraArrayList[mudraActivity.position].english.name.replace("\\n","\n")

        binding.note.text=mudraActivity.mudraArrayList[mudraActivity.position].english.note.replace("\\n","\n")

        binding.duration.text=mudraActivity.mudraArrayList[mudraActivity.position].english.duration.replace("\\n","\n")

        binding.steps.text=mudraActivity.mudraArrayList[mudraActivity.position].english.steps.replace("\\n","\n")

        binding.release.text=mudraActivity.mudraArrayList[mudraActivity.position].english.release.replace("\\n","\n")

        binding.definition.text=mudraActivity.mudraArrayList[mudraActivity.position].english.definition.replace("\\n","\n")

        binding.benfits.text=mudraActivity.mudraArrayList[mudraActivity.position].english.benefits.replace("\\n","\n")

        Glide.with(mudraActivity).load(mudraActivity.mudraArrayList[mudraActivity.position].english.imgUrl).into(binding.img)

        return binding.root
    }


}