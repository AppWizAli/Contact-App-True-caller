package com.atul.truecallercontactapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.provider.CallLog
import androidx.recyclerview.widget.DiffUtil

class RecentCallsAdapter(private var calls: List<CallLogModel>) :
    RecyclerView.Adapter<RecentCallsAdapter.CallViewHolder>() {

    fun updateList(newList: List<CallLogModel>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = calls.size
            override fun getNewListSize() = newList.size

            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
                // date is the unique timestamp from the system
                return calls[oldPos].date == newList[newPos].date
            }

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
                return calls[oldPos] == newList[newPos]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.calls = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val layout = when (viewType) {
            CallLog.Calls.INCOMING_TYPE -> R.layout.item_recent_incoming_call
            CallLog.Calls.MISSED_TYPE -> R.layout.item_recent_missed_call
            else -> R.layout.item_recent_outgoing_call
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return CallViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return calls[position].callType
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        val call = calls[position]

        // Show the Name (this fixes the "random number" issue from your screenshot)
        holder.tvNumber.text = if (call.name == "Unknown" || call.name.isNullOrEmpty()) call.number else call.name

        // This shows "15 minutes ago" instead of "Just now"
        holder.tvTime.text = call.time

        val typeText = when(call.callType) {
            CallLog.Calls.INCOMING_TYPE -> "Incoming"
            CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
            else -> "Missed"
        }

        // Using the duration we formatted in PhoneFragment
        holder.tvTitleDuration.text = "$typeText · ${call.label} · ${call.duration}"
    }

    override fun getItemCount() = calls.size

    class CallViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitleDuration: TextView = view.findViewById(R.id.tvTitleDuration)
        val tvNumber: TextView = view.findViewById(R.id.tvNumber)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
    }
}