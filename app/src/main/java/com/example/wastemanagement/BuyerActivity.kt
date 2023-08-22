package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.CheckBox
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BuyerActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer)

        firestore = FirebaseFirestore.getInstance()

        val button = findViewById<Button>(R.id.next_button)

        button.setOnClickListener {
            val selectedWasteTypesWithPrices = mutableMapOf<String, Double>()

            if (findViewById<CheckBox>(R.id.plastic_checkbox).isChecked) {
                val price = findViewById<EditText>(R.id.plastic_price_edit).text.toString().toDoubleOrNull()
                if (price != null) {
                    selectedWasteTypesWithPrices["Plastic"] = price
                }
            }

            if (findViewById<CheckBox>(R.id.paper_checkbox).isChecked) {
                val price = findViewById<EditText>(R.id.paper_price_edit).text.toString().toDoubleOrNull()
                if (price != null) {
                    selectedWasteTypesWithPrices["Paper"] = price
                }
            }
            if (findViewById<CheckBox>(R.id.metal_checkbox).isChecked) {
                val price = findViewById<EditText>(R.id.metal_price_edit).text.toString().toDoubleOrNull()
                if (price != null) {
                    selectedWasteTypesWithPrices["Metal"] = price
                }
            }

            if (findViewById<CheckBox>(R.id.organic_checkbox).isChecked) {
                val price = findViewById<EditText>(R.id.organic_price_edit).text.toString().toDoubleOrNull()
                if (price != null) {
                    selectedWasteTypesWithPrices["Organic"] = price
                }
            }
            if (findViewById<CheckBox>(R.id.glass_checkbox).isChecked) {
                val price = findViewById<EditText>(R.id.glass_price_edit).text.toString().toDoubleOrNull()
                if (price != null) {
                    selectedWasteTypesWithPrices["Glass"] = price
                }
            }

            if (findViewById<CheckBox>(R.id.wood_checkbox).isChecked) {
                val price = findViewById<EditText>(R.id.wood_price_edit).text.toString().toDoubleOrNull()
                if (price != null) {
                    selectedWasteTypesWithPrices["Wood"] = price
                }
            }


            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid

            if (userId != null) {
                val userDocRef = firestore.collection("Buyers").document(userId)

                // Update the user document with selected waste types
                userDocRef.update("selectedWasteTypesWithPrices", selectedWasteTypesWithPrices)
                    .addOnSuccessListener {
                        val intent = Intent(this, BuyerHomeActivity::class.java)
                        startActivity(intent)
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
            }
        }
    }
}
