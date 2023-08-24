package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BuyerActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var selectedWasteTypesWithPrices: Map<String, Double> // To store selected waste types and prices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer)

        firestore = FirebaseFirestore.getInstance()

        val button = findViewById<Button>(R.id.next_button)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val userDocRef = firestore.collection("Buyers").document(userId)

            // Fetch user data from Firestore
            userDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.data
                    if (data != null) {
                        val selectedWasteTypesWithPricesRaw = data["selectedWasteTypesWithPrices"]
                        if (selectedWasteTypesWithPricesRaw is Map<*, *>) {
                            selectedWasteTypesWithPrices = selectedWasteTypesWithPricesRaw as Map<String, Double>
                            populatePrices(selectedWasteTypesWithPrices)

                            // Call a function to populate checkboxes based on selected waste types
                            populateCheckboxes(selectedWasteTypesWithPrices.keys)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }


        button.setOnClickListener {
            val updatedWasteTypesWithPrices = mutableMapOf<String, Double>()

            // Loop through EditTexts and update the map with new prices
            val wasteTypeMap = mapOf(
                "Plastic" to R.id.plastic_checkbox,
                "Paper" to R.id.paper_checkbox,
                "Metal" to R.id.metal_checkbox,
                "Organic" to R.id.organic_checkbox,
                "Glass" to R.id.glass_checkbox,
                "Wood" to R.id.wood_checkbox
            )

            for ((wasteType, checkboxId) in wasteTypeMap) {
                val checkBox = findViewById<CheckBox>(checkboxId)
                val priceEdit = findViewById<EditText>(wasteTypeMap[wasteType]!! + 1)

                if (checkBox.isChecked) {
                    val price = priceEdit.text.toString().toDoubleOrNull()
                    if (price != null) {
                        updatedWasteTypesWithPrices[wasteType] = price
                    }
                }
            }

            // Update the user document with the updated waste types and prices
            if (userId != null) {
                val userDocRef = firestore.collection("Buyers").document(userId)
                userDocRef.update("selectedWasteTypesWithPrices", updatedWasteTypesWithPrices)
                    .addOnSuccessListener {
                        val intent = Intent(this, BuyerHomeActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    // Function to populate checkboxes based on selected waste types
    private fun populateCheckboxes(selectedWasteTypes: Set<String>) {
        val wasteTypeMap = mapOf(
            "Plastic" to R.id.plastic_checkbox,
            "Paper" to R.id.paper_checkbox,
            "Metal" to R.id.metal_checkbox,
            "Organic" to R.id.organic_checkbox,
            "Glass" to R.id.glass_checkbox,
            "Wood" to R.id.wood_checkbox
        )

        for ((wasteType, checkboxId) in wasteTypeMap) {
            val checkBox = findViewById<CheckBox>(checkboxId)

            // Set the checked state of the checkbox based on selectedWasteTypes
            checkBox.isChecked = selectedWasteTypes.contains(wasteType)
        }
    }

    private fun populatePrices(selectedWasteTypesWithPrices: Map<String, Double>) {
        for ((wasteType, price) in selectedWasteTypesWithPrices) {
            val priceEdit = findViewById<EditText>(resources.getIdentifier("${wasteType.toLowerCase()}_price_edit", "id", packageName))
            priceEdit.setText(price.toString())
        }
    }
}
