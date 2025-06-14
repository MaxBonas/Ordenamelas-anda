package com.example.routes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/** Simple adapter that displays a list of addresses. */
class AddressAdapter(private val items: MutableList<String>) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    class AddressViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.textView.text = items[position]
    }

    fun addAddress(address: String) {
        items.add(address)
        notifyItemInserted(items.size - 1)
    }
}
