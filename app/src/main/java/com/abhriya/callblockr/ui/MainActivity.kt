package com.abhriya.callblockr.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.abhriya.callblockr.R
import com.abhriya.callblockr.databinding.ActivityMainBinding
import com.abhriya.callblockr.service.ForegroundKeepAppAliveService
import com.abhriya.callblockr.ui.allcontacts.ContactListFragment
import com.abhriya.callblockr.ui.blockedcontacts.BlockedContactsFragment
import com.abhriya.callblockr.ui.calllog.CallLogFragment
import com.iammert.library.readablebottombar.ReadableBottomBar
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startKeepAppAliveService()
        decorateViewPager()
        initBottomBarListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_menu, menu)
        return true
    }

    fun startKeepAppAliveService() {
        Intent(this, ForegroundKeepAppAliveService::class.java).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(this, it);
            } else {
                startService(it)
            }
        }
    }

    private fun decorateViewPager() {
        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager, FragmentPagerItems.with(this)
                .add(R.string.blocked_contacts_fragment_name, BlockedContactsFragment::class.java)
//                .add(
//                    R.string.call_log_fragment_name,
//                    CallLogFragment::class.java
//                )
                .add(
                    R.string.unblocked_contacts_fragment_name,
                    ContactListFragment::class.java
                )
                .create()
        )
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.bottomBar.selectItem(position)
            }

        })
    }

    private fun initBottomBarListener() {
        binding.bottomBar.setOnItemSelectListener(object : ReadableBottomBar.ItemSelectListener {
            override fun onItemSelected(index: Int) {
                binding.viewPager.setCurrentItem(index, true)
            }
        })
    }

}