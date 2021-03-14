package com.dbtechprojects.autosmsmarketing.ui

import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.dbtechprojects.autosmsmarketing.R
import com.dbtechprojects.autosmsmarketing.db.entities.PhoneNumber
import kotlinx.android.synthetic.main.phone_number_item.view.*

class PhoneNumberAdapter(
    var items: List<PhoneNumber>,

    ): RecyclerView.Adapter<PhoneNumberAdapter.PhoneNumberViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class PhoneNumberViewHolder(itemview: View): RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneNumberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.phone_number_item, parent,false)
        return PhoneNumberViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhoneNumberViewHolder, position: Int) {
        val PhoneNumber = items[position]

        holder.itemView.rv_item_tv_PhoneNumber.text = PhoneNumber.number

        if(PhoneNumber.messagesent == 1){
            holder.itemView.rv_item_iv_message.setImageResource(R.drawable.ic_baseline_message_success_24)
            holder.itemView.rv_item_tv_SendMessage.text = "Message Sent"
        }

        holder.itemView.rv_item_iv_message.setOnClickListener {

            if (onClickListener != null) {
                onClickListener!!.onClick(position, PhoneNumber, holder.itemView)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: PhoneNumber, view: View)
    }
}

