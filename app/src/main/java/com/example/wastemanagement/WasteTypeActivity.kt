package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        val weightsMap = hashMapOf<String, Double>()

        val button = findViewById<Button>(R.id.confirm_button)

        button.setOnClickListener {
            val selectedWasteType: String
            var weightString: String
            var weight: Double = 0.0

            when {
                findViewById<RadioButton>(R.id.sellplastic).isChecked -> {
                    selectedWasteType = "Plastic"
                    weightString = findViewById<EditText>(R.id.plasticWeight).text.toString()
                    if (weightString.isBlank()) {
                        // Weight not entered, show a Toast message
                        Toast.makeText(this, "Please enter the weight", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    else{
                    weightsMap["Plastic"] = findViewById<EditText>(R.id.plasticWeight).text.toString().toDouble()}
                }
                findViewById<RadioButton>(R.id.sellpaper).isChecked -> {
                    selectedWasteType = "Paper"
                    weightString = findViewById<EditText>(R.id.paperWeight).text.toString()
                    if (weightString.isBlank()) {
                        // Weight not entered, show a Toast message
                        Toast.makeText(this, "Please enter the weight", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    else{
                    weightsMap["Paper"] = findViewById<EditText>(R.id.paperWeight).text.toString().toDouble()}
                }
                findViewById<RadioButton>(R.id.sellorganic).isChecked -> {
                    selectedWasteType = "Organic"
                    weightString = findViewById<EditText>(R.id.organicWeight).text.toString()
                    if (weightString.isBlank()) {
                        // Weight not entered, show a Toast message
                        Toast.makeText(this, "Please enter the weight", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    else{
                    weightsMap["Organic"] = findViewById<EditText>(R.id.organicWeight).text.toString().toDouble()}
                }
                findViewById<RadioButton>(R.id.sellglass).isChecked -> {
                    selectedWasteType = "Glass"
                    weightString = findViewById<EditText>(R.id.glassWeight).text.toString()
                    if (weightString.isBlank()) {
                        // Weight not entered, show a Toast message
                        Toast.makeText(this, "Please enter the weight", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    else{
                    weightsMap["Glass"] = findViewById<EditText>(R.id.glassWeight).text.toString().toDouble()}
                }
                findViewById<RadioButton>(R.id.sellmetal).isChecked -> {
                    selectedWasteType = "Metal"
                    weightString = findViewById<EditText>(R.id.metalWeight).text.toString()
                    if (weightString.isBlank()) {
                        // Weight not entered, show a Toast message
                        Toast.makeText(this, "Please enter the weight", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    else{
                    weightsMap["Metal"] = findViewById<EditText>(R.id.metalWeight).text.toString().toDouble()}
                }
                findViewById<RadioButton>(R.id.sellwood).isChecked -> {
                    selectedWasteType = "Wood"
                    weightString = findViewById<EditText>(R.id.woodWeight).text.toString()
                    if (weightString.isBlank()) {
                        // Weight not entered, show a Toast message
                        Toast.makeText(this, "Please enter the weight", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    else{
                    weightsMap["Wood"] = findViewById<EditText>(R.id.woodWeight).text.toString().toDouble()}
                }
                else -> {
                    // No radio button selected, show a Toast message
                    Toast.makeText(this, "Please select a waste type", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            try {
                weight = weightString.toDouble()
            } catch (e: NumberFormatException) {
                // Handle the case where weightString couldn't be converted to Double
                Toast.makeText(this, "Invalid weight format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            println("Assigned weight value: $weight")


            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid

            if (userId != null) {
                val userDocRef = firestore.collection("Sellers").document(userId)

                userDocRef.get().addOnSuccessListener { documentSnapshot ->
                    userDocRef.update(
                        "weight", weight,
                        "selectedWasteType", selectedWasteType
                    ).addOnSuccessListener {
                        val intent = Intent(this, DisplayBuyersActivity::class.java)
                        intent.putExtra("selectedWasteType", selectedWasteType)
                        intent.putExtra("weight", weight)
                        startActivity(intent)
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Error: User does not exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



