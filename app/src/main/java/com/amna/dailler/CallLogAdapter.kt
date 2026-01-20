package com.amna.dailler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.amna.dailler.databinding.ItemCallLogBinding

class CallLogAdapter(
    private val onItemClick: (CallLogEntry) -> Unit,
    private val onInfoClick: (CallLogEntry) -> Unit,
    private val onItemLongClick: (CallLogEntry) -> Unit
) : ListAdapter<CallLogEntry, CallLogAdapter.ViewHolder>(CallLogDiffCallback()) {

    class ViewHolder(val binding: ItemCallLogBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCallLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = getItem(position)
        holder.binding.tvName.text = entry.name ?: entry.number
        holder.binding.tvNumber.text = entry.number
        holder.binding.tvTime.text = entry.time
        
        val context = holder.binding.root.context
        
        when (entry.type) {
            CallType.MISSED -> {
                holder.binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.missed_call_red))
                holder.binding.ivCallType.setColorFilter(ContextCompat.getColor(context, R.color.missed_call_red))
            }
            CallType.INCOMING -> {
                holder.binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.text_primary_light))
                holder.binding.ivCallType.setColorFilter(ContextCompat.getColor(context, R.color.call_green))
            }
            CallType.OUTGOING -> {
                holder.binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.text_primary_light))
                holder.binding.ivCallType.setColorFilter(ContextCompat.getColor(context, R.color.primary_blue))
            }
            else -> {}
        }

        holder.binding.root.setOnClickListener { onItemClick(entry) }
        holder.binding.ivInfo.setOnClickListener { onInfoClick(entry) }
        holder.binding.root.setOnLongClickListener {
            onItemLongClick(entry)
            true
        }
    }

    class CallLogDiffCallback : DiffUtil.ItemCallback<CallLogEntry>() {
        override fun areItemsTheSame(oldItem: CallLogEntry, newItem: CallLogEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CallLogEntry, newItem: CallLogEntry): Boolean {
            return oldItem == newItem
        }
    }
}
