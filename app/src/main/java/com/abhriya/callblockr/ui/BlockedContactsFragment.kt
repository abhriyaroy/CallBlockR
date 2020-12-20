package com.abhriya.callblockr.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblockr.R
import com.abhriya.callblockr.databinding.FragmentBlockedContactsBinding
import com.abhriya.callblockr.domain.model.ContactModel
import com.abhriya.callblockr.domain.model.ContactModelType
import com.abhriya.callblockr.util.*
import com.abhriya.commons.DialogHelper
import com.abhriya.commons.InputValueListener
import com.abhriya.commons.SystemPermissionUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val BLOCKED_CONTACTS_FRAGMENT_PERMISSION_REQUEST_VALUE = 3

@AndroidEntryPoint
class BlockedContactsFragment : Fragment(),
    HandleItemClick {

    @Inject
    internal lateinit var dialogHelper: DialogHelper

    @Inject
    internal lateinit var systemPermissionUtil: SystemPermissionUtil
    val viewModel: BlockedContactsViewModel by viewModels()
    private var _binding: FragmentBlockedContactsBinding? = null
    private val binding get() = _binding!!
    private var isViewLocallyUpdated = false
    private lateinit var recyclerViewAdapter: ContactListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlockedContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        attachClickListeners()
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        checkForPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun handleActionImageClick(
        position: Int,
        contactModel: ContactModel
    ) {
        isViewLocallyUpdated = true
        viewModel.unblockContact(contactModel, viewModel.blockedContactLiveData)
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

    private fun checkForPermission(){
        systemPermissionUtil.checkPermissions(
            requireContext(),
            getListOfRequiredPermissions()
        ).also {
            if (systemPermissionUtil.getMissingPermissionsArray(it).isNotEmpty()) {
                obtainPermission()
            } else {
                binding.permissionRequiredLayout.permissionRequiredViewGroup.gone()
                binding.fab.visible()
                viewModel.getAllBlockedContacts()
            }
        }
    }

    private fun obtainPermission() {
        systemPermissionUtil.checkPermissions(
            requireContext(),
            getListOfRequiredPermissions()
        ).filter {
            !it.second
        }.apply {
            if (!isNullOrEmpty()) {
                requestPermissions(
                    map { it.first }.toTypedArray(),
                    BLOCKED_CONTACTS_FRAGMENT_PERMISSION_REQUEST_VALUE
                )
            }
        }
    }

    private fun attachClickListeners() {
        binding.fab.setOnClickListener {
            dialogHelper.showInputDialog(
                requireContext(),
                requireContext().stringRes(R.string.input_number_to_block),
                requireContext().stringRes(R.string.block_number),
                InputType.TYPE_CLASS_PHONE,
                object : InputValueListener {
                    override fun onInputSubmitted(inputText: String) {
                        viewModel.blockContact(
                            ContactModel(
                                phoneNumber = inputText,
                                contactModelType = ContactModelType.BLOCKED_CONTACT
                            ),
                            viewModel.blockedContactLiveData
                        )
                    }
                }
            )
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
        observeBlockedContactList()
        viewModel.getAllBlockedContacts()
    }

    private fun observeBlockedContactList() {
        viewModel.blockedContactLiveData.reObserve(
            this,
            Observer {
                handleDataUpdate(it)
            })
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
    }

    private fun handleSuccessState(result: List<ContactModel>) {
        binding.lottieLoader.pauseAnimation()
        binding.lottieLoader.gone()
        binding.stateView.gone()
        binding.recyclerView.visible()
        binding.recyclerView.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(
                context,
                R.anim.recyclerview_layout_anim
            )
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

    private fun getListOfRequiredPermissions(): List<String> {
        val requiredPermissions: MutableList<String> =
            mutableListOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE
//                Manifest.permission.READ_CALL_LOG
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requiredPermissions.add(Manifest.permission.ANSWER_PHONE_CALLS)
        }
        return requiredPermissions
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