package com.example.wastemanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BuyersAdapter : RecyclerView.Adapter<BuyersAdapter.BuyerViewHolder>() {

    private var buyersList = mutableListOf<Buyer>()
    private var selectedPosition = RecyclerView.NO_POSITION

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun getSelectedBuyer(): Buyer? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            buyersList[selectedPosition]
        } else {
            null
        }
    }

    fun setData(data: List<Buyer>) {
        buyersList.clear()
        buyersList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_buyer, parent, false)
        return BuyerViewHolder(view, this) // Pass the adapter reference to the ViewHolder
    }

    override fun onBindViewHolder(holder: BuyerViewHolder, position: Int) {
        val buyer = buyersList[position]
        holder.bind(buyer)
    }

    override fun getItemCount(): Int {
        return buyersList.size
    }

    class BuyerViewHolder(itemView: View, private val adapter: BuyersAdapter) : RecyclerView.ViewHolder(itemView) {
        private val buyerNameTextView: TextView = itemView.findViewById(R.id.buyer_name_text)
        private val priceTextView: TextView = itemView.findViewById(R.id.price_text)
        private val emailTextView: TextView = itemView.findViewById(R.id.email_text)
        private val phoneTextView: TextView = itemView.findViewById(R.id.phone_text)
        private val defaultBackground = itemView.background
        private val selectedBackground = itemView.context.getDrawable(R.drawable.selected_buyer_background)


        fun bind(buyer: Buyer) {
            buyerNameTextView.text = buyer.name
            priceTextView.text = "${buyer.price} per Kg"
            emailTextView.text = "Email: ${buyer.email}" // Set email
            phoneTextView.text = "Phone: ${buyer.phone}" // Set phone

            if (adapterPosition == adapter.selectedPosition) {
                itemView.background = selectedBackground
            } else {
                itemView.background = defaultBackground
            }
        }

        init {
            itemView.setOnClickListener {
                val oldPosition = adapter.selectedPosition
                val newPosition = adapterPosition
                if (newPosition != RecyclerView.NO_POSITION) {
                    adapter.setSelectedPosition(newPosition)

                }
            }
        }
    }
}

