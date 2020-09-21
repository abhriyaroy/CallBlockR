package com.abhriya.callblocker.ui.unblockedcontact

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblocker.R
import com.abhriya.callblocker.databinding.FragmentUnblockedContactsBinding
import com.abhriya.callblocker.domain.model.ContactModel
import com.abhriya.callblocker.ui.adapter.ContactListAdapter
import com.abhriya.callblocker.ui.adapter.HandleItemClick
import com.abhriya.callblocker.viewmodel.ContactsViewModel
import com.abhriya.callblocker.viewmodel.ResourceResult
import com.abhriya.callblocker.viewmodel.Status
import com.abhriya.commons.util.gone
import com.abhriya.commons.util.reObserve
import com.abhriya.commons.util.stringRes
import com.abhriya.commons.util.visible
import com.abhriya.systempermissions.SystemPermissionUtil
import com.abhriya.systempermissions.SystemPermissionsHandler
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class UnBlockedContactsFragment : Fragment(), HandleItemClick {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
        AndroidSupportInjection.inject(this)
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
            LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        recyclerViewAdapter = ContactListAdapter(requireContext(), this)
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(
            requireActivity(),
            viewModelFactory
        )[ContactsViewModel::class.java]
    }

    private fun attachClickListeners() {
        binding.grantPermissionButton.setOnClickListener {
            obtainPermission()
        }
    }

    private fun observeUnBlockedContactList() {
        viewModel.savedAvailableContactLiveData.reObserve(
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

    private fun handleDataUpdate(result: ResourceResult<List<ContactModel>>) {
        when (result.status) {
            Status.LOADING -> handleLoadingState()
            Status.SUCCESS -> handleSuccessState(result)
            Status.ERROR -> handleErrorState()
        }
    }

    private fun handleLoadingState() {
        binding.stateView.gone()
        binding.lottieLoader.playAnimation()
        binding.lottieLoader.visible()
        binding.recyclerView.gone()
        binding.grantPermissionButton.gone()
    }

    private fun handleSuccessState(result: ResourceResult<List<ContactModel>>) {
        binding.lottieLoader.pauseAnimation()
        binding.lottieLoader.gone()
        binding.stateView.gone()
        binding.recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recyclerview_layout_anim)
        binding.recyclerView.visible()
        if (result.data!!.isEmpty()) {
            showEmptyState()
        }
        recyclerViewAdapter.setContactsList(result.data)
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