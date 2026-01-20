package com.amna.dailler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.amna.dailler.databinding.ItemContactBinding

class ContactAdapter(
    private val onClick: (Contact) -> Unit
) : ListAdapter<Contact, ContactAdapter.ViewHolder>(ContactDiffCallback()) {

    class ViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = getItem(position)
        holder.binding.tvName.text = contact.name
        holder.binding.tvNumber.text = contact.number
        holder.binding.root.setOnClickListener { onClick(contact) }
        
        // 3-dot menu click handler
        holder.binding.ivMore.setOnClickListener { view ->
            val popup = androidx.appcompat.widget.PopupMenu(view.context, view)
            popup.menu.add("Call ${contact.number}")
            popup.menu.add("Send message")
            popup.menu.add("View contact")
            popup.menu.add("Add to favorites")
            
            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Call ${contact.number}" -> {
                        val intent = android.content.Intent(android.content.Intent.ACTION_CALL).apply {
                            data = android.net.Uri.parse("tel:${contact.number}")
                        }
                        view.context.startActivity(intent)
                    }
                    "Send message" -> {
                        val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                            data = android.net.Uri.parse("smsto:${contact.number}")
                        }
                        view.context.startActivity(intent)
                    }
                    "View contact" -> {
                        onClick(contact)
                    }
                    "Add to favorites" -> {
                        android.widget.Toast.makeText(view.context, "Add to favorites", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            popup.show()
        }
    }

    class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}
