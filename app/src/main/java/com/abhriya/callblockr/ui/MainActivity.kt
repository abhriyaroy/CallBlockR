package com.abhriya.callblockr.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.abhriya.callblockr.R
import com.abhriya.callblockr.databinding.ActivityMainBinding
import com.abhriya.callblockr.service.ForegroundKeepAppAliveService
import com.abhriya.callblockr.util.openAppSettings
import com.abhriya.commons.util.stringRes
import com.google.android.material.snackbar.Snackbar
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
        menuInflater.inflate(R.menu.bottom_menu,menu)
        return true
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        for (i in permissions.indices) {
//            val permission = permissions[i]
//            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                val showRationale =
//                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
//                if (!showRationale) {
//                    showOpenSettingsSnackBar(this, binding.rootLayout)
//                } else {
//                    showGrantPermissionSnackBar(
//                        this,
//                        binding.rootLayout,
//                        permissions.toList()
//                    )
//                }
//            }
//        }
//    }

    private fun decorateViewPager() {
        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager, FragmentPagerItems.with(this)
                .add(R.string.blocked_contacts_fragment_name, BlockedContactsFragment::class.java)
                .add(
                    R.string.call_log_fragment_name,
                    CallLogFragment::class.java
                )
                .add(
                    R.string.unblocked_contacts_fragment_name,
                    ContactListFragment::class.java
                )
                .create()
        )
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
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

    private fun startKeepAppAliveService() {
        with(Intent(this, ForegroundKeepAppAliveService::class.java)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(this)
            } else {
                startService(this)
            }
        }
    }

    private fun showOpenSettingsSnackBar(
        activity: Activity,
        coordinatorLayout: CoordinatorLayout
    ) {
        Snackbar.make(
            coordinatorLayout,
            activity.stringRes(R.string.accept_permission_from_settings),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(
            activity.stringRes(R.string.open_settings)
        ) {
            openAppSettings()
        }.show()
    }

//    private fun showGrantPermissionSnackBar(
//        activity: Activity,
//        coordinatorLayout: CoordinatorLayout,
//        permissionList: List<String>
//    ) {
//        Snackbar.make(
//            coordinatorLayout,
//            activity.stringRes(R.string.accept_permission),
//            Snackbar.LENGTH_INDEFINITE
//        ).setAction(
//            activity.stringRes(R.string.grant_permission)
//        ) {
//            permissionsHandler.requestPermission(
//                activity, permissionList.map { it to false }
//            )
//        }.show()
//    }

    private fun initBottomBarListener(){
        binding.bottomBar.setOnItemSelectListener( object : ReadableBottomBar.ItemSelectListener{
            override fun onItemSelected(index: Int) {
                binding.viewPager.setCurrentItem(index, true)
            }
        })
    }

}