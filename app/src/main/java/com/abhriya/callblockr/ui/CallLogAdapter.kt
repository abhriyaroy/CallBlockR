package com.abhriya.callblockr.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abhriya.callblockr.R
import com.abhriya.callblockr.data.entity.CallType
import com.abhriya.callblockr.data.entity.ContactEntity
import com.abhriya.callblockr.domain.model.CallLogModel
import com.abhriya.callblockr.domain.model.ContactModel
import com.abhriya.callblockr.domain.model.ContactModelType
import com.abhriya.callblockr.util.drawableRes
import com.abhriya.callblockr.util.stringRes
import kotlinx.android.synthetic.main.item_call_log.view.*
import kotlinx.android.synthetic.main.item_call_log.view.actionImage
import kotlinx.android.synthetic.main.item_contacts_list.view.*
import kotlinx.android.synthetic.main.item_contacts_list.view.contactAvatar
import kotlinx.android.synthetic.main.item_contacts_list.view.rootLayout
import kotlinx.android.synthetic.main.item_contacts_list.view.subTitleTextView
import kotlinx.android.synthetic.main.item_contacts_list.view.titleTextView

class CallLogAdapter(
    private val context: Context,
    private val handleCallLogItemClick: HandleCallLogItemClick
) : RecyclerView.Adapter<CallLogViewHolder>() {

    private var list: MutableList<CallLogModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        return CallLogViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_call_log, parent, false
            ), context,
            handleCallLogItemClick
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.apply {
            decorateItem(list[position])
            attachActionItemClickListener(list[position])
        }
    }

    fun setCallLogList(callLogList: List<CallLogModel>) {
        val oldList = list
        list = callLogList.toMutableList()
        val diffResult = DiffUtil.calculateDiff(
            CallLogDiffUtilCallback(
                list,
                oldList
            )
        )
        diffResult.dispatchUpdatesTo(this)
    }

}

class CallLogViewHolder(
    itemView: View,
    private val context: Context,
    private val handleCallLogItemClick: HandleCallLogItemClick
) :
    RecyclerView.ViewHolder(itemView) {

    fun decorateItem(callLogModel: CallLogModel) {
        itemView.titleTextView.text = if(callLogModel.contactName.isNotBlank()){
            callLogModel.contactName
        } else {
            context.stringRes(R.string.unknown)
        }
        itemView.subTitleTextView.text = callLogModel.contactNumber
        itemView.contactAvatar.setText(itemView.titleTextView.text.toString())
        when (callLogModel.callType) {
            CallType.INCOMING_CALL-> {
                itemView.callTypeTextView.text = context.stringRes(R.string.incoming)
            }
            CallType.OUTGOING_CALL-> {
                itemView.callTypeTextView.text = context.stringRes(R.string.outgoing)
            }
            CallType.MISSED_CALL-> {
                itemView.callTypeTextView.text = context.stringRes(R.string.missed)
            }
            CallType.VOICE_MAIL-> {
                itemView.callTypeTextView.text = context.stringRes(R.string.voiceMail)
            }
            CallType.REJECTED_CALL-> {
                itemView.callTypeTextView.text = context.stringRes(R.string.rejected)
            }
            CallType.BLOCKED_CALL-> {
                itemView.callTypeTextView.text = context.stringRes(R.string.blocked)
            }
        }
        if(!callLogModel.isNumberBlocked){
            itemView.actionImage.setImageDrawable(
                context.drawableRes(
                    R.drawable.ic_black_block_24
                ))
        } else {
            itemView.actionImage.setImageDrawable(null)
        }
    }

    fun attachActionItemClickListener(callLogModel: CallLogModel) {
        itemView.rootLayout.setOnClickListener {
            handleCallLogItemClick.handleActionImageClick(adapterPosition, callLogModel)
        }
    }
}

interface HandleCallLogItemClick {
    fun handleActionImageClick(position: Int, callLogModel: CallLogModel)
}

class CallLogDiffUtilCallback(
    private val newList: List<CallLogModel>,
    private val oldList: List<CallLogModel>
) : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldItemPosition, newItemPosition)
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].contactNumber == newList[newItemPosition].contactNumber
                && oldList[oldItemPosition].timeStampInMillis == newList[newItemPosition].timeStampInMillis
                && oldList[oldItemPosition].isNumberBlocked == newList[newItemPosition].isNumberBlocked
    }

}