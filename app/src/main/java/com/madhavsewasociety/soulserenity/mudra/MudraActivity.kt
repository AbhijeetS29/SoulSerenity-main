package com.madhavsewasociety.soulserenity.mudra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.madhavsewasociety.soulserenity.databinding.ActivityMudraBinding

class MudraActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMudraBinding
    private lateinit var mudraTabAdapter: MudraTabAdapter
    lateinit var mudraArrayList : ArrayList<Languages>
    var position : Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMudraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mudraArrayList=intent.getSerializableExtra("mudra") as ArrayList<Languages>
        position=intent.getIntExtra("pos",0)

        mudraTabAdapter = MudraTabAdapter(supportFragmentManager, lifecycle)
        tabLayout()

    }
    private fun tabLayout(){
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("In English"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("In Punjabi"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("In Hindi"))
        binding.viewPager.adapter = mudraTabAdapter

        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!=null){
                    binding.viewPager.currentItem=tab.position
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }
}