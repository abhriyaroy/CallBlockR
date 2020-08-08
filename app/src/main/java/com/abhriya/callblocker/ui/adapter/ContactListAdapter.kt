package com.abhriya.callblocker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblocker.R
import com.abhriya.callblocker.domain.model.ContactModel
import com.abhriya.callblocker.domain.model.ContactModelType
import com.abhriya.callblocker.util.ContactListDiffUtilCallback
import com.abhriya.callblocker.util.drawableRes
import com.abhriya.callblocker.util.stringRes
import kotlinx.android.synthetic.main.item_contacts_list.view.*

class ContactListAdapter(private val handleItemClick: HandleItemClick) :
    RecyclerView.Adapter<ContactListViewHolder>() {

    private var list: MutableList<ContactModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        return ContactListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_contacts_list, parent, false
            ), parent.context,
            handleItemClick
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.apply {
            decorateItem(list[position])
            attachActionItemClickListener(list[position])
        }
    }

    fun setContactsList(contactModelList: List<ContactModel>) {
        val oldList = list
        list = contactModelList.toMutableList()
        val diffResult = DiffUtil.calculateDiff(ContactListDiffUtilCallback(list, oldList))
        diffResult.dispatchUpdatesTo(this)

    }

}

class ContactListViewHolder(
    itemView: View,
    private val context: Context,
    private val handleItemClick: HandleItemClick
) :
    RecyclerView.ViewHolder(itemView) {

    fun decorateItem(contactModel: ContactModel) {
        itemView.titleTextView.text = contactModel.name ?: context.stringRes(R.string.unknown)
        itemView.subTitleTextView.text = contactModel.phoneNumber
        when (contactModel.contactModelType) {
            ContactModelType.BLOCKED_CONTACT -> itemView.actionImage.setImageDrawable(
                context.drawableRes(
                    R.drawable.ic_black_remove_24
                )
            )
            ContactModelType.UNBLOCKED_CONTACT -> itemView.actionImage.setImageDrawable(
                context.drawableRes(
                    R.drawable.ic_black_block_24
                )
            )

        }
    }

    fun attachActionItemClickListener(contactModel: ContactModel) {
        itemView.actionImage.setOnClickListener {
            handleItemClick.handleActionImageClick(adapterPosition, contactModel)
        }
    }
}

interface HandleItemClick {
    fun handleActionImageClick(position: Int, contactModel: ContactModel)
}