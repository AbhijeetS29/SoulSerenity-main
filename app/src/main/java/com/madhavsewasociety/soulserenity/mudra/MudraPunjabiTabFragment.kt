package com.madhavsewasociety.soulserenity.mudra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.madhavsewasociety.soulserenity.databinding.FragmentMudraPunjabiTabBinding

class MudraPunjabiTabFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentMudraPunjabiTabBinding
    private lateinit var mudraActivity: MudraActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMudraPunjabiTabBinding.inflate(layoutInflater, container, false)
        mudraActivity=activity as MudraActivity

        binding.titleMudra.text=mudraActivity.mudraArrayList[mudraActivity.position].punjabi.name.replace("\\n","\n")

        binding.note.text=mudraActivity.mudraArrayList[mudraActivity.position].punjabi.note.replace("\\n","\n")

        binding.duration.text=mudraActivity.mudraArrayList[mudraActivity.position].punjabi.duration.replace("\\n","\n")

        binding.steps.text=mudraActivity.mudraArrayList[mudraActivity.position].punjabi.steps.replace("\\n","\n")

        binding.release.text=mudraActivity.mudraArrayList[mudraActivity.position].punjabi.release.replace("\\n","\n")

        binding.definition.text=mudraActivity.mudraArrayList[mudraActivity.position].punjabi.definition.replace("\\n","\n")

        binding.benfits.text=mudraActivity.mudraArrayList[mudraActivity.position].punjabi.benefits.replace("\\n","\n")

        Glide.with(mudraActivity).load(mudraActivity.mudraArrayList[mudraActivity.position].punjabi.imgUrl).into(binding.img)
        return binding.root
    }
}