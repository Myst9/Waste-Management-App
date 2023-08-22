package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WasteTypeActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waste_type)

        firestore = FirebaseFirestore.getInstance()

        val button = findViewById<Button>(R.id.confirm_button)

        button.setOnClickListener {
            val selectedWasteType: String
            val weight: String

            when {
                findViewById<RadioButton>(R.id.sellplastic).isChecked -> {
                    selectedWasteType = "Plastic"
                    weight = findViewById<EditText>(R.id.plasticWeight).text.toString()
                }
                findViewById<RadioButton>(R.id.sellpaper).isChecked -> {
                    selectedWasteType = "Paper"
                    weight = findViewById<EditText>(R.id.paperWeight).text.toString()
                }
                findViewById<RadioButton>(R.id.sellorganic).isChecked -> {
                    selectedWasteType = "Organic"
                    weight = findViewById<EditText>(R.id.organicWeight).text.toString()
                }
                findViewById<RadioButton>(R.id.sellglass).isChecked -> {
                    selectedWasteType = "Glass"
                    weight = findViewById<EditText>(R.id.glassWeight).text.toString()
                }
                findViewById<RadioButton>(R.id.sellmetal).isChecked -> {
                    selectedWasteType = "Metal"
                    weight = findViewById<EditText>(R.id.metalWeight).text.toString()
                }
                findViewById<RadioButton>(R.id.sellwood).isChecked -> {
                    selectedWasteType = "Wood"
                    weight = findViewById<EditText>(R.id.woodWeight).text.toString()
                }
                else -> {
                    // No radio button selected, handle this case as needed
                    return@setOnClickListener
                }
            }

            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid

            if (userId != null) {
                val userDocRef = firestore.collection("Sellers").document(userId)

                // Update the user document with weight and waste type
                userDocRef.update(
                    "weight", weight,
                    "wasteType", selectedWasteType
                ).addOnSuccessListener {
                    val intent = Intent(this, DisplayBuyersActivity::class.java)
                    intent.putExtra("selectedWasteType", selectedWasteType) // Pass the selected waste type here
                    startActivity(intent)
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
            }
        }
    }
}

