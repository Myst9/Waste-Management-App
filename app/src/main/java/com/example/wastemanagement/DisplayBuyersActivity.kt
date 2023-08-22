package com.example.wastemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.RecyclerView
import android.view.View


class DisplayBuyersActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BuyersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_buyers)

        firestore = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recycler_view)

        // Get the selected waste type from the intent
        val sellerSelectedWasteType = intent.getStringExtra("selectedWasteType")

        // Set up the RecyclerView with an adapter
        adapter = BuyersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Query buyers collection to find buyers interested in the same waste type
        firestore.collection("Buyers")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val buyersList = mutableListOf<Buyer>()

                for (document in querySnapshot.documents) {
                    val buyerId = document.id // Get the document ID
                    val buyerName = document.getString("Name")
                    val selectedWasteTypesMap = document.get("selectedWasteTypesWithPrices") as Map<*, *>?
                    val email = document.getString("Email") // Add this line
                    val phone = document.getString("Phone") // Add this line

                    if (selectedWasteTypesMap != null) {
                        for ((wasteType, price) in selectedWasteTypesMap) {
                            if (wasteType == sellerSelectedWasteType && price is Double && buyerName != null) {
                                val buyer = Buyer(buyerId, buyerName, price, email ?: "", phone ?: "")
                                buyersList.add(buyer)
                            }
                        }
                    }
                }

                adapter.setData(buyersList)
            }
            .addOnFailureListener { e ->
                val errorMessage = "Error fetching buyers: ${e.message}"
                Log.e("DisplayBuyersActivity", errorMessage)
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }


    }
    fun sendRequest(view: View) {
        val selectedBuyer = adapter.getSelectedBuyer()

        if (selectedBuyer != null) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val currentUserId = currentUser?.uid

            if (currentUserId != null) {
                val request = hashMapOf(
                    "sellerId" to currentUserId,
                    "buyerId" to selectedBuyer.id,
                    "status" to false
                )

                firestore.collection("requests")
                    .add(request)
                    .addOnSuccessListener {
                        val buyerName = selectedBuyer.name
                        Toast.makeText(this, "Request sent to $buyerName", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error sending request: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No buyer selected", Toast.LENGTH_SHORT).show()
        }
    }


}



