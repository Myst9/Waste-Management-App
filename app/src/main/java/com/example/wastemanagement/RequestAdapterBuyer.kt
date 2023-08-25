package com.example.wastemanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RequestAdapterBuyer : ListAdapter<Request, RequestAdapterBuyer.RequestViewHolder>(RequestDiffCallback()) {
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request_buyer, parent, false)
        return RequestViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = getItem(position)
        holder.bind(request)
    }

    inner class RequestViewHolder(itemView: View, private val adapter: RequestAdapterBuyer) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        private val sellerDetailsTextView: TextView = itemView.findViewById(R.id.sellerDetailsTextView)
        private val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        private val rejectButton: Button = itemView.findViewById(R.id.rejectButton)

        fun bind(request: Request) {
            titleTextView.text = "Request ID: ${request.documentId}" // Display Request ID
            statusTextView.text = "Status: ${request.status}"

            val selectedWasteType = request.selectedWasteType // Access the selected waste type directly from the Request object

            val sellerId = request.sellerId
            val sellerRef = firestore.collection("Sellers").document(sellerId)

            sellerRef.get().addOnSuccessListener { sellerDocument ->
                val sellerName = sellerDocument.getString("Name")
                val sellerEmail = sellerDocument.getString("Email")
                val sellerPhone = sellerDocument.getString("Phone")
                val sellerAddress1 = sellerDocument.getString("AddressLine1")
                val sellerAddress2 = sellerDocument.getString("AddressLine2")
                val sellerCity = sellerDocument.getString("City")
                val sellerPincode = sellerDocument.getString("Pincode")

                val sellerDetailsText = "Seller: $sellerName\n" +
                        "Email: $sellerEmail\n" +
                        "Phone: $sellerPhone\n" +
                        "Address Line 1: $sellerAddress1\n" +
                        "Address Line 2: $sellerAddress2\n" +
                        "City: $sellerCity\n" +
                        "Pincode: $sellerPincode\n" +
                        "Selected Waste Type: $selectedWasteType"
                sellerDetailsTextView.text = sellerDetailsText
            }

            acceptButton.setOnClickListener {
                request.status = "accepted"
                adapter.notifyItemChanged(adapterPosition)
                updateAcceptStatus(request)
            }

            rejectButton.setOnClickListener {
                request.status = "rejected"
                adapter.notifyItemChanged(adapterPosition)
                updateRejectStatus(request)
            }
        }
    }



    private fun updateAcceptStatus(request: Request) {
        val newStatus = "accepted"
        val requestId = request.documentId

        if (requestId.isNotBlank()) {
            val requestRef = firestore.collection("requests").document(requestId)

            requestRef.update("status", newStatus)
                .addOnSuccessListener {
                    notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    // Handle failure
                }
        }
    }

    private fun updateRejectStatus(request: Request) {
        val newStatus = "rejected"
        val requestId = request.documentId

        if (requestId.isNotBlank()) {
            val requestRef = firestore.collection("requests").document(requestId)

            requestRef.update("status", newStatus)
                .addOnSuccessListener {
                    notifyDataSetChanged()
                }

        }
    }

    private class RequestDiffCallback : DiffUtil.ItemCallback<Request>() {
        override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem.documentId == newItem.documentId
        }

        override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem == newItem
        }
    }
}
