package com.abhriya.callblockr.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblockr.R
import com.abhriya.callblockr.domain.model.ContactModel
import com.abhriya.callblockr.domain.model.ContactModelType
import com.abhriya.commons.util.*
import kotlinx.android.synthetic.main.item_call_log.view.*
import kotlinx.android.synthetic.main.item_contacts_list.view.*
import kotlinx.android.synthetic.main.item_contacts_list.view.actionImage
import kotlinx.android.synthetic.main.item_contacts_list.view.contactAvatar
import kotlinx.android.synthetic.main.item_contacts_list.view.rootLayout
import kotlinx.android.synthetic.main.item_contacts_list.view.subTitleTextView
import kotlinx.android.synthetic.main.item_contacts_list.view.titleTextView

class ContactListAdapter(
    private val context: Context,
    private val handleItemClick: HandleItemClick
) : RecyclerView.Adapter<ContactListViewHolder>() {

    private var list: MutableList<ContactModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        return ContactListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_contacts_list, parent, false
            ), context,
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
        val diffResult = DiffUtil.calculateDiff(
            ContactListDiffUtilCallback(
                list,
                oldList
            )
        )
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
        itemView.contactAvatar.setText(itemView.titleTextView.text.toString())
        when (contactModel.contactModelType) {
            ContactModelType.BLOCKED_CONTACT -> itemView.actionImage.setImageDrawable(
                context.drawableRes(
                    R.drawable.ic_black_remove_24
                )
            )
            ContactModelType.ALL_CONTACT -> {
                if (!contactModel.isContactBlocked) {
                    itemView.actionImage.setImageDrawable(
                        context.drawableRes(
                            R.drawable.ic_black_block_24
                        )
                    )
                    itemView.actionImage.visible()
                } else {
                    itemView.actionImage.invisible()

                }
            }
        }
    }

    fun attachActionItemClickListener(contactModel: ContactModel) {
        if (!contactModel.isContactBlocked) {
            itemView.rootLayout.setOnClickListener {
                handleItemClick.handleActionImageClick(adapterPosition, contactModel)
            }
        }
    }
}

interface HandleItemClick {
    fun handleActionImageClick(position: Int, contactModel: ContactModel)
}

class ContactListDiffUtilCallback(
    private val newList: List<ContactModel>,
    private val oldList: List<ContactModel>
) : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldItemPosition, newItemPosition)
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].phoneNumber == newList[newItemPosition].phoneNumber
                && oldList[oldItemPosition].name == newList[newItemPosition].name
                && oldList[oldItemPosition].name == newList[newItemPosition].name
                && oldList[oldItemPosition].isContactBlocked == newList[newItemPosition].isContactBlocked
    }

}