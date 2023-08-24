package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserAddressActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_address)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val saveAddressButton = findViewById<Button>(R.id.save_address_button)

        saveAddressButton.setOnClickListener {
            val addressLine1 = findViewById<EditText>(R.id.address_line1).text.toString()
            val addressLine2 = findViewById<EditText>(R.id.address_line2).text.toString()
            val city = findViewById<EditText>(R.id.city).text.toString()
            val pincode = findViewById<EditText>(R.id.pincode).text.toString()

            if (addressLine1.isEmpty() || city.isEmpty() || pincode.isEmpty()) {
                Toast.makeText(this, "Please fill in all address fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUser = auth.currentUser
            val userId = currentUser?.uid

            if (userId != null) {
                val addressData = hashMapOf<String, Any>(
                    "AddressLine1" to addressLine1,
                    "AddressLine2" to addressLine2,
                    "City" to city,
                    "Pincode" to pincode
                )

                val userDocRef = firestore.collection("Sellers").document(userId)
                userDocRef.update(addressData)
                firestore.collection("Sellers")
                    .document(userId)
                    .update(addressData)
                    .addOnSuccessListener {
                        val intent = Intent(this@UserAddressActivity, SellerHomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                val userDocRefBuyer = firestore.collection("Buyers").document(userId)
                userDocRefBuyer.update(addressData)
                firestore.collection("Buyers")
                    .document(userId)
                    .update(addressData)
                    .addOnSuccessListener {
                        val intent = Intent(this@UserAddressActivity, BuyerActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

            }
        }
    }
}
