package com.abhriya.callblocker.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.abhriya.callblocker.R
import com.abhriya.callblocker.databinding.ActivityMainBinding
import com.abhriya.callblocker.service.ForegroundKeepAppAliveService
import com.abhriya.callblocker.ui.blockedcontact.BlockedContactsFragment
import com.abhriya.callblocker.ui.unblockedcontact.UnBlockedContactsFragment
import com.abhriya.commons.util.stringRes
import com.abhriya.systempermissions.SystemPermissionsHandler
import com.google.android.material.snackbar.Snackbar
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
        startKeepAppAliveService()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in permissions.indices) {
            val permission = permissions[i]
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                val showRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                if (!showRationale) {
                    showOpenSettingsSnackBar(this, binding.rootLayout)
                } else {
                    showGrantPermissionSnackBar(
                        this,
                        binding.rootLayout,
                        permissions.toList()
                    )
                }
            }
        }
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

    private fun startKeepAppAliveService() {
        with(Intent(this, ForegroundKeepAppAliveService::class.java)) {
            startService(this)
        }
    }

    private fun showOpenSettingsSnackBar(
        activity: Activity,
        coordinatorLayout: CoordinatorLayout
    ) {
        Snackbar.make(
            coordinatorLayout,
            activity.stringRes(com.abhriya.systempermissions.R.string.accept_permission_from_settings),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(
            activity.stringRes(com.abhriya.systempermissions.R.string.open_settings)
        ) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity.packageName)
            )
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        }.show()
    }

    private fun showGrantPermissionSnackBar(
        activity: Activity,
        coordinatorLayout: CoordinatorLayout,
        permissionList: List<String>
    ) {
        Snackbar.make(
            coordinatorLayout,
            activity.stringRes(com.abhriya.systempermissions.R.string.accept_permission),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(
            activity.stringRes(com.abhriya.systempermissions.R.string.grant_permission)
        ) {
            requestPermission(
                activity, permissionList.map { it to false }
            )
        }.show()
    }

}