package com.abhriya.callblockr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblockr.domain.model.ContactModel
import com.abhriya.callblockr.domain.model.ContactModelType
import com.abhriya.callblockr.util.drawableRes
import com.abhriya.callblockr.util.stringRes
import kotlinx.android.synthetic.main.item_contacts_list.view.*

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
            ContactModelType.UNBLOCKED_CONTACT -> itemView.actionImage.setImageDrawable(
                context.drawableRes(
                    R.drawable.ic_black_block_24
                )
            )
        }
    }

    fun attachActionItemClickListener(contactModel: ContactModel) {
        itemView.rootLayout.setOnClickListener {
            handleItemClick.handleActionImageClick(adapterPosition, contactModel)
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
        return oldList[oldItemPosition].phoneNumber == newList[newItemPosition].phoneNumber
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].phoneNumber == newList[newItemPosition].phoneNumber
                && oldList[oldItemPosition].name == newList[newItemPosition].name
    }

}