package com.abhriya.callblocker.ui.blockedcontact

import android.graphics.Canvas
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblocker.R
import com.abhriya.callblocker.databinding.FragmentBlockedContactsBinding
import com.abhriya.callblocker.domain.model.ContactModel
import com.abhriya.callblocker.domain.model.ContactModelType
import com.abhriya.callblocker.domain.model.ContactModelType.BLOCKED_CONTACT
import com.abhriya.callblocker.ui.adapter.ContactListAdapter
import com.abhriya.callblocker.ui.adapter.HandleItemClick
import com.abhriya.callblocker.util.*
import com.abhriya.callblocker.viewmodel.ContactsViewModel
import com.abhriya.callblocker.viewmodel.ResourceResult
import com.abhriya.callblocker.viewmodel.Status
import com.abhriya.commons.DialogHelper
import com.abhriya.commons.InputValueListener
import com.abhriya.commons.RecyclerViewSwipeDecorator
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class BlockedContactsFragment : Fragment(), HandleItemClick {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var dialogHelper: DialogHelper
    private var _binding: FragmentBlockedContactsBinding? = null
    private val binding get() = _binding!!
    private var isViewLocallyUpdated = false
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
        viewModel.unblockContact(contactModel)
//        recyclerViewAdapter.removeItem(position)
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
                            )
                        )
                    }
                }
            )
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        recyclerViewAdapter = ContactListAdapter(this)
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[ContactsViewModel::class.java]
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
            Status.LOADING -> {
                binding.lottieLoader.visible()
                binding.lottieLoader.playAnimation()
            }
            Status.SUCCESS -> {
                binding.lottieLoader.pauseAnimation()
                binding.lottieLoader.gone()
                recyclerViewAdapter.setContactsList(result.data!!)
            }
            Status.ERROR -> {
            }
        }
    }
}