package com.abhriya.callblockr

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblockr.databinding.FragmentUnblockedContactsBinding
import com.abhriya.callblockr.domain.model.ContactModel
import com.abhriya.callblockr.viewmodel.ContactsViewModel
import com.abhriya.callblockr.util.ResourceState
import com.abhriya.callblockr.util.gone
import com.abhriya.callblockr.util.stringRes
import com.abhriya.callblockr.util.visible
import com.abhriya.systempermissions.SystemPermissionUtil
import com.abhriya.systempermissions.SystemPermissionsHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactListFragment : Fragment(), HandleItemClick {

    @Inject
    internal lateinit var systemPermissionsHandler: SystemPermissionsHandler

    @Inject
    internal lateinit var systemPermissionUtil: SystemPermissionUtil

    private var _binding: FragmentUnblockedContactsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ContactsViewModel
    private lateinit var recyclerViewAdapter: ContactListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUnblockedContactsBinding.inflate(inflater, container, false)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun handleActionImageClick(position: Int, contactModel: ContactModel) {
        viewModel.blockContact(contactModel, viewModel.savedAvailableContactLiveData)
    }


    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        recyclerViewAdapter = ContactListAdapter(requireContext(), this)
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun initViewModel() {
        viewModel = requireActivity().run {
            ViewModelProvider(this)[ContactsViewModel::class.java]
        }
    }

    private fun attachClickListeners() {
        binding.grantPermissionButton.setOnClickListener {
            obtainPermission()
        }
    }

    private fun observeUnBlockedContactList() {
        viewModel.savedAvailableContactLiveData.observe(
            this,
            Observer {
                handleDataUpdate(it)
            })
    }

    private fun checkForPermission() {
        systemPermissionsHandler.checkPermissions(
            requireContext(),
            getListOfRequiredPermissions()
        ).also {
            if (systemPermissionUtil.filterPermissionListForMissingPermissions(it).isNotEmpty()) {
                showGrantPermissionLayout()
            } else {
                loadUnblockedContacts()
            }
        }
    }

    private fun obtainPermission() {
        systemPermissionsHandler.checkPermissions(
            requireContext(),
            getListOfRequiredPermissions()
        ).apply {
            systemPermissionUtil.filterPermissionListForMissingPermissions(this)
                .apply {
                    if (isNotEmpty()) {
                        systemPermissionsHandler.requestPermission(
                            requireActivity(),
                            this
//                    this@UnBlockedContactsFragment
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
            grantPermissionButton.visible()
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
        binding.grantPermissionButton.gone()
    }

    private fun handleSuccessState(result: List<ContactModel>) {
        binding.lottieLoader.pauseAnimation()
        binding.lottieLoader.gone()
        binding.stateView.gone()
        binding.recyclerView.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.recyclerview_layout_anim)
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
}