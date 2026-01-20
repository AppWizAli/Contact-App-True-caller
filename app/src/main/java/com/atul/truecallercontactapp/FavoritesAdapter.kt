package com.atul.truecallercontactapp

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class FavoritesAdapter(private val favorites: List<ContactModel>) :
    RecyclerView.Adapter<FavoritesAdapter.FavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val contact = favorites[position]
        holder.txtName.text = contact.name
        holder.txtInitial.text = contact.name.take(1).uppercase()

        // logic to vary colors like in the screenshot
        val colors = intArrayOf(R.color.teal, R.color.purple_500, R.color.orange_500)
        val color = colors[position % colors.size]
        holder.txtInitial.background.setColorFilter(
            ContextCompat.getColor(holder.itemView.context, color),
            PorterDuff.Mode.SRC_IN
        )
    }

    override fun getItemCount() = favorites.size

    class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtInitial: TextView = view.findViewById(R.id.txtInitial)
        val txtName: TextView = view.findViewById(R.id.txtName)
    }
}