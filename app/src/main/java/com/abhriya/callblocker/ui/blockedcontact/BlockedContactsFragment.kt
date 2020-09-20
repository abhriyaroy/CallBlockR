package com.abhriya.callblocker.ui.blockedcontact

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblocker.R
import com.abhriya.callblocker.databinding.FragmentBlockedContactsBinding
import com.abhriya.callblocker.domain.model.ContactModel
import com.abhriya.callblocker.domain.model.ContactModelType.BLOCKED_CONTACT
import com.abhriya.callblocker.ui.adapter.ContactListAdapter
import com.abhriya.callblocker.ui.adapter.HandleItemClick
import com.abhriya.callblocker.viewmodel.ContactsViewModel
import com.abhriya.callblocker.viewmodel.ResourceResult
import com.abhriya.callblocker.viewmodel.Status
import com.abhriya.commons.DialogHelper
import com.abhriya.commons.InputValueListener
import com.abhriya.commons.util.gone
import com.abhriya.commons.util.reObserve
import com.abhriya.commons.util.stringRes
import com.abhriya.commons.util.visible
import com.abhriya.systempermissions.SystemPermissionsHandler
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class BlockedContactsFragment : Fragment(), HandleItemClick {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var dialogHelper: DialogHelper

    @Inject
    internal lateinit var systemPermissionsHandler: SystemPermissionsHandler
    private var _binding: FragmentBlockedContactsBinding? = null
    private val binding get() = _binding!!
    private var isViewLocallyUpdated = false
    private lateinit var viewModel: ContactsViewModel
    private lateinit var recyclerViewAdapter: ContactListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        obtainPermission()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun handleActionImageClick(position: Int, contactModel: ContactModel) {
        isViewLocallyUpdated = true
        viewModel.unblockContact(contactModel, viewModel.blockedContactLiveData)
    }

    private fun obtainPermission() {
        systemPermissionsHandler.checkPermissions(
            requireContext(),
            getListOfRequiredPermissions()
        ).apply {
            println("the list $this")
            if (isNotEmpty()) {
                systemPermissionsHandler.requestPermission(
                    requireActivity(),
                    this
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
                                contactModelType = BLOCKED_CONTACT
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
            LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        recyclerViewAdapter = ContactListAdapter(requireContext(), this)
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)[ContactsViewModel::class.java]
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
    }

    private fun handleSuccessState(result: ResourceResult<List<ContactModel>>) {
        binding.lottieLoader.pauseAnimation()
        binding.lottieLoader.gone()
        binding.stateView.gone()
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

    private fun getListOfRequiredPermissions(): List<String> {
        val requiredPermissions: MutableList<String> =
            mutableListOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requiredPermissions.add(Manifest.permission.ANSWER_PHONE_CALLS)
        }
        return requiredPermissions
    }
}