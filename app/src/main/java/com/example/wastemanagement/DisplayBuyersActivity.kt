package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        // Set up the RecyclerView with an adapter
        adapter = BuyersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            firestore.collection("Sellers")
                .document(userId)
                .get()
                .addOnSuccessListener { sellerDocument ->
                    val sellerPincode = sellerDocument.getString("Pincode")

                    // Get the selected waste type from the intent
                    val sellerSelectedWasteType = intent.getStringExtra("selectedWasteType")

                    if (sellerPincode != null) {
                        firestore.collection("Buyers")
                            .whereEqualTo("Pincode", sellerPincode) // Filter based on pincode
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                val buyersList = mutableListOf<Buyer>()

                                for (document in querySnapshot.documents) {
                                    val buyerId = document.id
                                    val buyerName = document.getString("Name")
                                    val selectedWasteTypesMap = document.get("selectedWasteTypesWithPrices") as Map<*, *>?
                                    val email = document.getString("Email")
                                    val phone = document.getString("Phone")
                                    val pincode = document.getString("Pincode") // Retrieve the pincode

                                    if (selectedWasteTypesMap != null && pincode != null) {
                                        Log.d("Debug", "$sellerSelectedWasteType")
                                        for ((wasteType, price) in selectedWasteTypesMap) {
                                            if (wasteType == sellerSelectedWasteType && price is Double && buyerName != null) {
                                                val buyer = Buyer(buyerId, buyerName, price, email ?: "", phone ?: "", pincode)
                                                Log.d("Debug", "$buyerName")
                                                buyersList.add(buyer)
                                            }
                                        }
//                                        if(buyersList.isEmpty()){
//                                            Toast.makeText(this, "No buyers found for the selected criteria.", Toast.LENGTH_SHORT).show()
//                                        }
                                    } else {
                                        Toast.makeText(this, "No buyers found in the nearest locations", Toast.LENGTH_SHORT).show()
                                        Log.d("DisplayBuyersActivity", "Missing selectedWasteTypesMap or Pincode")
                                    }
                                }

                                adapter.setData(buyersList)
                            }
                            .addOnFailureListener { e ->
                                val errorMessage = "Error fetching buyers: ${e.message}"
                                Log.e("DisplayBuyersActivity", errorMessage)
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Seller's pincode not found.", Toast.LENGTH_SHORT).show()
                        Log.e("DisplayBuyersActivity", "Seller's pincode not found.")
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error retrieving seller information: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
    fun sendRequest(view: View) {
        val selectedBuyer = adapter.getSelectedBuyer()

        if (selectedBuyer != null) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val currentUserId = currentUser?.uid

            if (currentUserId != null) {
                val selectedWasteType = intent.getStringExtra("selectedWasteType")
                val weight = intent.getStringExtra("weight")

                val request = hashMapOf(
                    "sellerId" to currentUserId,
                    "buyerId" to selectedBuyer.id,
                    "selectedWasteType" to selectedWasteType,
                    "weight" to weight,
                    "status" to "pending"
                )

                firestore.collection("requests")
                    .add(request)
                    .addOnSuccessListener { documentReference ->
                        // Get the generated document ID
                        val requestId = documentReference.id

                        // Update the request document to include the documentId
                        firestore.collection("requests")
                            .document(requestId)
                            .update("documentId", requestId)
                            .addOnSuccessListener {
                                // Create a Request object using the retrieved data
                                val requestObj = Request(
                                    documentId = requestId,
                                    buyerId = selectedBuyer.id,
                                    sellerId = currentUserId,
                                    status = "pending"
                                )
                                val buyerName = selectedBuyer.name
                                Toast.makeText(this, "Request sent to $buyerName", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@DisplayBuyersActivity, SellerHomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error updating document: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
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


