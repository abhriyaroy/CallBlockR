package com.abhriya.callblocker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.abhriya.callblocker.R
import com.abhriya.callblocker.databinding.ActivityMainBinding
import com.abhriya.callblocker.ui.blockedcontact.BlockedContactsFragment
import com.abhriya.callblocker.ui.unblockedcontact.UnBlockedContactsFragment
import com.abhriya.systempermissions.SystemPermissionsHandler
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    internal lateinit var systemPermissionsHandler: SystemPermissionsHandler

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        decorateViewPager()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        systemPermissionsHandler.onPermissionResult(
            this,
            binding.rootLayout as CoordinatorLayout,
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun supportFragmentInjector() = fragmentInjector

    private fun decorateViewPager() {
        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager, FragmentPagerItems.with(this)
                .add(R.string.blocked_contacts_fragment_name, BlockedContactsFragment::class.java)
                .add(
                    R.string.unblocked_contacts_fragment_name,
                    UnBlockedContactsFragment::class.java
                )
                .create()
        )
        binding.viewPager.adapter = adapter
        binding.viewPagerTab.setViewPager(binding.viewPager)
    }

}