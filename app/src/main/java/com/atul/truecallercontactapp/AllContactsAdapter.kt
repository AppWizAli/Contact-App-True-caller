package com.atul.truecallercontactapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class AllContactsAdapter(private var contactList: List<ContactModel>) :
    RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder>() {

    fun updateList(newList: List<ContactModel>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = contactList.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
                // Using phone number as unique identifier
                return contactList[oldPos].phoneNumber == newList[newPos].phoneNumber
            }
            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
                return contactList[oldPos] == newList[newPos]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        contactList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]

        // Match these with your ContactModel fields
        holder.txtName.text = contact.name

        // If you want to show the number but don't have a TextView for it yet,
        // you should add one to your XML. For now, this only sets the name.
    }

    override fun getItemCount(): Int = contactList.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // MATCHED TO YOUR XML IDs
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
    }
}