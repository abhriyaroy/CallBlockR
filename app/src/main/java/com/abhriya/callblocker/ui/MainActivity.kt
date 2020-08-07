package com.abhriya.callblocker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abhriya.callblocker.R
import com.abhriya.callblocker.databinding.ActivityMainBinding
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        decorateViewPager()
    }

    private fun decorateViewPager() {
        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager, FragmentPagerItems.with(this)
                .add(
                    R.string.unblocked_contacts_fragment_name,
                    UnBlockedContactsFragment::class.java
                )
                .add(R.string.blocked_contacts_fragment_name, BlockedContactsFragment::class.java)
                .create()
        )

        binding.viewPager.adapter = adapter
        binding.viewPagerTab.setViewPager(binding.viewPager)
    }

}