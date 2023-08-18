package com.example.wastemanagement

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val signUp = findViewById<Button>(R.id.signup)
        val signInText = findViewById<TextView>(R.id.signinoption)

        signUp.setOnClickListener {
            val userName = findViewById<EditText>(R.id.name).text.toString()
            val userEmail = findViewById<EditText>(R.id.email).text.toString()
            val userPassword = findViewById<EditText>(R.id.password).text.toString()
            val userPhone = findViewById<EditText>(R.id.phoneNo).text.toString()
            val sell = findViewById<RadioButton>(R.id.sellButton)
            val buy = findViewById<RadioButton>(R.id.buyButton)
            val userType = if (sell.isChecked) "seller" else "buyer"

            if (userName.isBlank() || userEmail.isBlank() || userPassword.isBlank() || userPhone.isBlank()) {
                Toast.makeText(
                    this@MainActivity,
                    "Please fill all the fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (!sell.isChecked && !buy.isChecked) {
                AlertDialog.Builder(this)
                    .setTitle("Select Option")
                    .setMessage("Please select either 'Sell Waste' or 'Buy Waste'")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })
                    .show()
            }
            else {
                auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.let {
                                val userData = hashMapOf(
                                    "Name" to userName,
                                    "Email" to userEmail,
                                    "Phone" to userPhone
                                )

                                val userCollection = if (userType == "seller") "Sellers" else "Buyers"
                                firestore.collection(userCollection)
                                    .document(user.uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        if(userType=="seller")
                                        {val intent = Intent(this, HomeActivity::class.java)
                                        intent.putExtra("USER_ID", user.uid)
                                        startActivity(intent)}
                                        else
                                        {val intent = Intent(this, BuyerActivity::class.java)
                                            intent.putExtra("USER_ID", user.uid)
                                            startActivity(intent)}
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Failed to create user data in Firestore",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            // Handle Authentication failure
                            Toast.makeText(
                                this@MainActivity,
                                "Authentication failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        signInText.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}