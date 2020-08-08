package com.abhriya.callblocker.ui.unblockedcontact

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.abhriya.callblocker.databinding.FragmentUnBlockedContactsBinding
import com.abhriya.callblocker.viewmodel.ContactsViewModel
import com.abhriya.commons.util.gone
import com.abhriya.commons.util.visible
import com.abhriya.systempermissions.PermissionsCallback
import com.abhriya.systempermissions.SystemPermissionsHandler
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class UnBlockedContactsFragment : Fragment(), PermissionsCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var systemPermissionsHandler: SystemPermissionsHandler

    private var _binding: FragmentUnBlockedContactsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ContactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUnBlockedContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        attachClickListeners()
    }

    override fun onResume() {
        super.onResume()
        checkForPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun handlePermissionsResult() {
        loadUnblockedContacts()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[ContactsViewModel::class.java]
    }

    private fun attachClickListeners() {
        binding.grantPermissionButton.setOnClickListener {
            obtainPermission()
        }
    }

    private fun checkForPermission() {
        systemPermissionsHandler.getMissingPermissionListIfAnyOutOfSuppliedPermissionList(
            requireContext(),
            getListOfRequiredPermissions()
        ).also {
            if (it.isNotEmpty()) {
                showGrantPermissionLayout()
            } else {
                loadUnblockedContacts()
            }
        }
    }

    private fun obtainPermission() {
        systemPermissionsHandler.getMissingPermissionListIfAnyOutOfSuppliedPermissionList(
            requireContext(),
            getListOfRequiredPermissions()
        ).apply {
            if (isNotEmpty()) {
                systemPermissionsHandler.requestPermission(
                    requireActivity(),
                    this,
                    this@UnBlockedContactsFragment
                )
            }
        }
    }

    private fun getListOfRequiredPermissions(): List<String> {
        return mutableListOf(Manifest.permission.READ_CONTACTS)
    }

    private fun showGrantPermissionLayout() {
        binding.apply {
            grantPermissionButton.visible()
            lottieLoader.gone()
            recyclerView.gone()
        }
    }

    private fun loadUnblockedContacts() {

    }
}