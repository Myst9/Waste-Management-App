package com.example.wastemanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val itemList: List<Item>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // ViewHolder class
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemText: TextView = itemView.findViewById(R.id.itemText)
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2 // Alternates between 0 (left image) and 1 (right image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutRes = if (viewType == 0) {
            R.layout.item_list_left_image
        } else {
            R.layout.item_list_right_image
        }
        val view = layoutInflater.inflate(layoutRes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemImage.setImageResource(item.imageResource)
        holder.itemText.text = item.text
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
