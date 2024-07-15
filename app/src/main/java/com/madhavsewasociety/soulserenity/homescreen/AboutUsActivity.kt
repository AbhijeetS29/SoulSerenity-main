package com.madhavsewasociety.soulserenity.homescreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.madhavsewasociety.soulserenity.databinding.ActivityMudraBinding

class AboutUsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMudraBinding
    private lateinit var aboutUsAdapter: AboutUsTabAdapter
    var position : Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMudraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        position=intent.getIntExtra("pos",0)

        aboutUsAdapter = AboutUsTabAdapter(supportFragmentManager, lifecycle)
        tabLayout()

    }
    private fun tabLayout(){
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Madhav Sewa Society"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Vivek Joshi"))
        binding.viewPager.adapter = aboutUsAdapter

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