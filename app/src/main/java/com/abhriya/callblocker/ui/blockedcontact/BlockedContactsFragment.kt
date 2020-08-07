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
import com.abhriya.callblocker.ui.adapter.ContactListAdapter
import com.abhriya.callblocker.util.*
import com.abhriya.callblocker.viewmodel.ContactsViewModel
import com.abhriya.callblocker.viewmodel.ResourceResult
import com.abhriya.callblocker.viewmodel.Status
import com.abhriya.commons.DialogHelper
import com.abhriya.commons.InputValueListener
import com.abhriya.commons.RecyclerViewSwipeDecorator
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class BlockedContactsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var dialogHelper: DialogHelper
    private var _binding: FragmentBlockedContactsBinding? = null
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

    private fun attachClickListeners() {
        binding.fab.setOnClickListener {
            dialogHelper.showInputDialog(
                requireContext(),
                requireContext().stringRes(R.string.input_number_to_block),
                requireContext().stringRes(R.string.block_number),
                InputType.TYPE_CLASS_PHONE,
                object : InputValueListener {
                    override fun onInputSubmitted(inputText: String) {
                        viewModel.blockContact(ContactModel(phoneNumber = inputText))
                    }
                }
            )
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        recyclerViewAdapter = ContactListAdapter()
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.setHasFixedSize(true)
        ItemTouchHelper(getItemTouchHelperCallbackObject())
            .attachToRecyclerView(binding.recyclerView)
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

    private fun getItemTouchHelperCallbackObject(): ItemTouchHelper.SimpleCallback {
        return object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                    }
                    ItemTouchHelper.LEFT -> {
                    }
                }
            }

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                )
                getSendSwiper(
                    canvas, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive
                )
            }
        }
    }

    private fun getSendSwiper(
        canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float,
        dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) = RecyclerViewSwipeDecorator.Builder(
        canvas, recyclerView, viewHolder, dX, dY,
        actionState, isCurrentlyActive
    ).addSwipeLeftBackgroundColor(colorRes(R.color.green))
        .addSwipeLeftLabel(stringRes(R.string.unblock))
        .setSwipeLeftLabelColor(colorRes(R.color.white))
        .create()
        .decorate()
}