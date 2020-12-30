package com.abhriya.callblockr.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblockr.R
import com.abhriya.callblockr.databinding.FragmentCallLogBinding
import com.abhriya.callblockr.domain.model.CallLogModel
import com.abhriya.callblockr.domain.model.ContactModel
import com.abhriya.callblockr.domain.model.ContactModelType
import com.abhriya.callblockr.util.*
import com.abhriya.callblockr.viewmodel.ContactsViewModel
import com.abhriya.commons.SystemPermissionUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallLogFragment : Fragment(), HandleCallLogItemClick {

    @Inject
    internal lateinit var systemPermissionUtil: SystemPermissionUtil
    private lateinit var viewModel: ContactsViewModel
    private lateinit var binding: FragmentCallLogBinding
    private lateinit var recyclerViewAdapter: CallLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCallLogBinding.inflate(inflater, container, false)
        viewModel = requireActivity().run {
            ViewModelProvider(this)[ContactsViewModel::class.java]
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        attachClickListeners()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        checkForPermission()
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
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        permission
                    )
                if (!showRationale) {
                    showOpenSettingsSnackBar(requireActivity().findViewById(R.id.rootLayout))
//                } else {
//                    showGrantPermissionSnackBar(
//                        requireActivity().findViewById(R.id.rootLayout),
//                        permissions.toList()
//                    )
                }
            }
        }
    }

    override fun handleActionImageClick(position: Int, callLogModel: CallLogModel) {
        viewModel.blockContact(
            ContactModel(
                name = callLogModel.contactName,
                phoneNumber = callLogModel.contactNumber,
                contactModelType = ContactModelType.BLOCKED_CONTACT
            ),
            viewModel.blockedContactLiveData
        )
        showContactBlockedSnackBar(requireActivity().findViewById(R.id.rootLayout))
    }

    private fun setupObservers() {
        viewModel.callLogList.observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.setCallLogList(it)
        })

        viewModel.blockedContactLiveData.observe(viewLifecycleOwner, Observer {
            if(it is ResourceState.Success){
                checkForPermission()
            }
        })
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        recyclerViewAdapter =
            CallLogAdapter(requireContext(), this)
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun attachClickListeners() {
        binding.permissionRequiredLayout.permissionRequiredViewGroup.setOnClickListener {
            obtainPermission()
        }
    }

    private fun checkForPermission() {
        systemPermissionUtil.checkPermissions(
            requireContext(),
            getListOfRequiredPermissions()
        ).also {
            if (systemPermissionUtil.getMissingPermissionsArray(it).isNotEmpty()) {
                showGrantPermissionLayout()
            } else {
                viewModel.getCallLog()
                binding.permissionRequiredLayout.permissionRequiredViewGroup.gone()
                binding.recyclerView.visible()
            }
        }
    }

    private fun obtainPermission() {
        systemPermissionUtil.checkPermissions(
            requireContext(),
            getListOfRequiredPermissions()
        ).run {
            systemPermissionUtil.getMissingPermissionsArray(this)
                .apply {
                    if (isNotEmpty()) {
                        requestPermissions(
                            this,
                            12
                        )
                    }
                }
        }
    }

    private fun getListOfRequiredPermissions(): List<String> {
        return mutableListOf(Manifest.permission.READ_CALL_LOG)
    }

    private fun showGrantPermissionLayout() {
        binding.apply {
            permissionRequiredLayout.permissionRequiredViewGroup.visible()
        }
    }

    private fun showOpenSettingsSnackBar(coordinatorLayout: CoordinatorLayout) {
        Snackbar.make(
            coordinatorLayout,
            stringRes(R.string.accept_permission_from_settings),
            Snackbar.LENGTH_LONG
        ).setAction(
            stringRes(R.string.open_settings)
        ) {
            requireActivity().openAppSettings()
        }.show()
    }

    private fun showContactBlockedSnackBar(coordinatorLayout: CoordinatorLayout) {
        Snackbar.make(
            coordinatorLayout,
            stringRes(R.string.contact_blocked),
            Snackbar.LENGTH_SHORT
        ).show()
    }

}