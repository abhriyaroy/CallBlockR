package com.abhriya.callblockr.ui.blockcontact

import android.app.Dialog
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import com.abhriya.callblockr.R
import com.abhriya.callblockr.databinding.FragmentBlockContactBinding
import com.abhriya.callblockr.domain.model.ContactModel
import com.abhriya.callblockr.domain.model.ContactModelType
import com.abhriya.callblockr.viewmodel.ContactsViewModel
import com.abhriya.commons.util.KeyboardUtil
import com.abhriya.commons.util.stringRes
import com.abhriya.commons.util.withDelayOnMain
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockContactFragment : AAH_FabulousFragment() {

    lateinit var binding: FragmentBlockContactBinding
    private lateinit var viewModel: ContactsViewModel

    override fun setupDialog(dialog: Dialog, style: Int) {
        binding =
            FragmentBlockContactBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        viewModel = requireActivity().run {
            ViewModelProvider(this)[ContactsViewModel::class.java]
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.confirmButton.setOnClickListener {
            KeyboardUtil.hideKeyboard(it)
            withDelayOnMain(300) {
                viewModel.blockContact(
                    ContactModel(
                        phoneNumber = viewModel.inputNumberToBlock.value!!,
                        contactModelType = ContactModelType.BLOCKED_CONTACT
                    ),
                    viewModel.blockedContactLiveData
                )
                closeFilter("")
                viewModel.inputNumberToBlock.value = ""
                showContactBlockedSnackBar(requireActivity().findViewById(R.id.rootLayout))
            }
        }
        binding.closeImageView.setOnClickListener {
            closeFilter("")
            viewModel.inputNumberToBlock.value = ""
        }
        //params to set
        setAnimationDuration(300) //optional; default 500ms
//        setPeekHeight(300) // optional; default 400dp
//        setCallbacks(activity as Callbacks?) //optional; to get back result
//        setAnimationListener(activity as AnimationListener?) //optional; to get animation callbacks
//        setViewgroupStatic(ll_buttons) // optional; layout to stick at bottom on slide
//        setViewPager(vp_types) //optional; if you use viewpager that has scrollview
        setViewMain(binding.mainConstraintView) //necessary; main bottomsheet view
        setMainContentView(binding.root) // necessary; call at end before super
        super.setupDialog(dialog, style) //call super at last
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        closeFilter("")
        viewModel.inputNumberToBlock.value = ""
    }

    private fun showContactBlockedSnackBar(coordinatorLayout: CoordinatorLayout) {
        Snackbar.make(
            coordinatorLayout,
            stringRes(R.string.contact_blocked),
            Snackbar.LENGTH_SHORT
        ).show()
    }

}