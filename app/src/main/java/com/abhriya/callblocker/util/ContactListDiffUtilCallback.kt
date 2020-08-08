package com.abhriya.callblocker.util

import androidx.recyclerview.widget.DiffUtil
import com.abhriya.callblocker.domain.model.ContactModel

class ContactListDiffUtilCallback(
    private val newList: List<ContactModel>,
    private val oldList: List<ContactModel>
) : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].phoneNumber == newList[newItemPosition].phoneNumber
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].phoneNumber == newList[newItemPosition].phoneNumber
                && oldList[oldItemPosition].name == newList[newItemPosition].name
    }

}