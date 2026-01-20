package com.amna.dailler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amna.dailler.databinding.ItemAccountFilterBinding

data class AccountFilter(val name: String, val detail: String, val count: Int, var isChecked: Boolean = true)

class AccountFilterAdapter(private val accounts: List<AccountFilter>) :
    RecyclerView.Adapter<AccountFilterAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemAccountFilterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAccountFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val account = accounts[position]
        holder.binding.tvAccountName.text = account.name
        holder.binding.tvAccountDetail.text = account.detail
        holder.binding.tvCount.text = "(${account.count})"
        holder.binding.cbFilter.isChecked = account.isChecked
        
        holder.binding.root.setOnClickListener {
            account.isChecked = !account.isChecked
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = accounts.size
}
