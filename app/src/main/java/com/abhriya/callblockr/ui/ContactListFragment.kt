package com.abhriya.callblockr.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblockr.R
import com.abhriya.callblockr.databinding.FragmentContactListBinding
import com.abhriya.callblockr.domain.model.ContactModel
import com.abhriya.callblockr.util.*
import com.abhriya.callblockr.viewmodel.ContactsViewModel
import com.abhriya.commons.SystemPermissionUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactListFragment : Fragment(), HandleItemClick {

    @Inject
    internal lateinit var systemPermissionUtil: SystemPermissionUtil

    private lateinit var binding : FragmentContactListBinding
    private lateinit var viewModel: ContactsViewModel
    private lateinit var recyclerViewAdapter: ContactListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initViewModel()
        attachClickListeners()
        observeUnBlockedContactList()
    }

    override fun onResume() {
        super.onResume()
        checkForPermission()
    }

    override fun handleActionImageClick(position: Int, contactModel: ContactModel) {
        viewModel.blockContact(contactModel, viewModel.savedAvailableContactLiveData)
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
                } else {
                    showGrantPermissionSnackBar(
                        requireActivity().findViewById(R.id.rootLayout),
                        permissions.toList()
                    )
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        recyclerViewAdapter =
            ContactListAdapter(requireContext(), this)
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun initViewModel() {
        viewModel = requireActivity().run {
            ViewModelProvider(this)[ContactsViewModel::class.java]
        }
    }

    private fun attachClickListeners() {
        binding.permissionRequiredLayout.permissionRequiredViewGroup.setOnClickListener {
            Log.d("${this.javaClass.name}","layout clicked")
            obtainPermission()
        }
    }

    private fun observeUnBlockedContactList() {
        viewModel.savedAvailableContactLiveData.observe(
            viewLifecycleOwner,
            Observer {
                handleDataUpdate(it)
            })
    }

    private fun checkForPermission() {
        Log.d("${this.javaClass.name}","check permission")
        systemPermissionUtil.checkPermissions(
            requireContext(),
            getListOfRequiredPermissions()
        ).let {
            systemPermissionUtil.getMissingPermissionsArray(it)
        }.also {
            if (it.isNotEmpty()) {
                Log.d("${this.javaClass.name}","the permission list is ${it[0]}")
                showGrantPermissionLayout()
            } else {
                loadUnblockedContacts()
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
        return mutableListOf(Manifest.permission.READ_CONTACTS)
    }

    private fun showGrantPermissionLayout() {
        binding.apply {
            permissionRequiredLayout.permissionRequiredViewGroup.visible()
            lottieLoader.gone()
            recyclerView.gone()
        }
    }

    private fun loadUnblockedContacts() {
        viewModel.getAllSavedAvailableContacts()
    }

    private fun handleDataUpdate(result: ResourceState<List<ContactModel>>) {
        when (result) {
            is ResourceState.Loading -> handleLoadingState()
            is ResourceState.Success -> handleSuccessState(result.data)
            is ResourceState.Error -> handleErrorState()
        }
    }

    private fun handleLoadingState() {
        binding.stateView.gone()
        binding.lottieLoader.playAnimation()
        binding.lottieLoader.visible()
        binding.recyclerView.gone()
        binding.permissionRequiredLayout.permissionRequiredViewGroup.gone()
    }

    private fun handleSuccessState(result: List<ContactModel>) {
        binding.lottieLoader.pauseAnimation()
        binding.lottieLoader.gone()
        binding.stateView.gone()
        binding.recyclerView.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context,
                R.anim.recyclerview_layout_anim
            )
        binding.recyclerView.visible()
        if (result.isEmpty()) {
            showEmptyState()
        }
        recyclerViewAdapter.setContactsList(result)
    }

    private fun handleErrorState() {
        binding.recyclerView.gone()
        binding.stateView.text = stringRes(R.string.something_went_wrong)
        binding.stateView.visible()
    }

    private fun showEmptyState() {
        binding.stateView.text = stringRes(R.string.wow_so_empty)
        binding.stateView.visible()
    }

    private fun showOpenSettingsSnackBar(coordinatorLayout: CoordinatorLayout) {
        Snackbar.make(
            coordinatorLayout,
            stringRes(R.string.accept_permission_from_settings),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(
            stringRes(R.string.open_settings)
        ) {
            requireActivity().openAppSettings()
        }.show()
    }

    private fun showGrantPermissionSnackBar(
        coordinatorLayout: CoordinatorLayout,
        permissionList: List<String>
    ) {
        Snackbar.make(
            coordinatorLayout,
            stringRes(R.string.accept_permission),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(
            stringRes(R.string.grant_permission)
        ) {
            requestPermissions(
                permissionList.toTypedArray(),
                BLOCKED_CONTACTS_FRAGMENT_PERMISSION_REQUEST_VALUE
            )
        }.show()
    }
}