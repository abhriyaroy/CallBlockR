package com.abhriya.callblocker.ui

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abhriya.callblocker.R
import com.abhriya.callblocker.databinding.FragmentBlockedContactsBinding
import com.abhriya.callblocker.util.stringRes
import com.abhriya.commons.DialogHelper
import com.abhriya.commons.InputValueListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class BlockedContactsFragment : Fragment() {

    @Inject
    internal lateinit var dialogHelper: DialogHelper
    private var _binding: FragmentBlockedContactsBinding? = null
    private val binding get() = _binding!!

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
        attachClickListeners()
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
                        println(inputText)
                    }
                }
            )
        }
    }
}