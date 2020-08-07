package com.abhriya.callblocker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abhriya.callblocker.R
import com.abhriya.callblocker.databinding.FragmentBlockedContactsBinding
import com.abhriya.callblocker.databinding.FragmentUnBlockedContactsBinding
import dagger.android.support.AndroidSupportInjection


class UnBlockedContactsFragment : Fragment() {

    private var _binding: FragmentUnBlockedContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUnBlockedContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}