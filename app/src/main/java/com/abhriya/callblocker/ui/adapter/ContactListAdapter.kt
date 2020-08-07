package com.abhriya.callblocker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblocker.R
import com.abhriya.callblocker.domain.model.ContactModel
import kotlinx.android.synthetic.main.item_contacts_list.view.*

class ContactListAdapter : RecyclerView.Adapter<ContactListViewHolder>() {

    private var list: MutableList<ContactModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        return ContactListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_contacts_list, parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.decorateItem(list[position])
    }

    fun setContactsList(contactModelList: List<ContactModel>) {
        list = contactModelList.toMutableList()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(contactModel: ContactModel) {
        list.add(contactModel)
        notifyItemInserted(list.size - 1)
    }

}

class ContactListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun decorateItem(contactModel: ContactModel) {
        itemView.titleTextView.text = contactModel.name ?: ""
        itemView.subTitleTextView.text = contactModel.phoneNumber
    }
}