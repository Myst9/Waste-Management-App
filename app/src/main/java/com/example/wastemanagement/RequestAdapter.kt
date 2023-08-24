package com.example.wastemanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemanagement.R
import com.example.wastemanagement.Request
import com.google.firebase.firestore.FirebaseFirestore

class RequestAdapter : ListAdapter<Request, RequestAdapter.RequestViewHolder>(RequestDiffCallback()) {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = getItem(position)
        holder.bind(request)
    }

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        private val buyerDetailsTextView: TextView = itemView.findViewById(R.id.buyerDetailsTextView)
        private val sellerDetailsTextView: TextView = itemView.findViewById(R.id.sellerDetailsTextView)
        private val selectedWasteTypeTextView: TextView = itemView.findViewById(R.id.selectedWasteTypeTextView)

        fun bind(request: Request) {
            titleTextView.text = "Request ID: ${request.documentId}" // Display request ID
            statusTextView.text = "Status: ${request.status}"

            val buyerId = request.buyerId
            val sellerId = request.sellerId

            // Fetch buyer details
            val buyerRef = firestore.collection("Buyers").document(buyerId)
            buyerRef.get().addOnSuccessListener { buyerDocument ->
                val buyerName = buyerDocument.getString("Name")
                val buyerEmail = buyerDocument.getString("Email")
                val buyerPhone = buyerDocument.getString("Phone")

                val buyerDetailsText = "Buyer: $buyerName\n" +
                        "Email: $buyerEmail\n" +
                        "Phone: $buyerPhone"

                buyerDetailsTextView.text = buyerDetailsText
            }

            // Fetch seller details
            val sellerRef = firestore.collection("Sellers").document(sellerId)
            sellerRef.get().addOnSuccessListener { sellerDocument ->
                val sellerName = sellerDocument.getString("Name")
                val sellerEmail = sellerDocument.getString("Email")
                val sellerPhone = sellerDocument.getString("Phone")
                val selectedWasteType = sellerDocument.getString("selectedWasteType")

                val sellerDetailsText = "Seller: $sellerName\n" +
                        "Email: $sellerEmail\n" +
                        "Phone: $sellerPhone"

                sellerDetailsTextView.text = sellerDetailsText
                selectedWasteTypeTextView.text = "Selected Waste Type: $selectedWasteType"
            }
        }
    }

    private class RequestDiffCallback : DiffUtil.ItemCallback<Request>() {
        override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem.documentId == newItem.documentId && // Compare request ID
                    oldItem.sellerId == newItem.sellerId &&
                    oldItem.status == newItem.status
        }

        override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem == newItem
        }
    }
}





