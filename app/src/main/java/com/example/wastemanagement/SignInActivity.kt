package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val signIn = findViewById<Button>(R.id.signin)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)
        val register = findViewById<TextView>(R.id.tvRegister)

        signIn.setOnClickListener {
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this@SignInActivity,
                    "Please fill all the fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            // Check the user's collection to determine user type
                            firestore.collection("Sellers").document(user.uid).get()
                                .addOnSuccessListener { sellerDocument ->
                                    if (sellerDocument.exists()) {
                                        val intent = Intent(this, SellerHomeActivity::class.java)
                                        intent.putExtra("USER_ID", user.uid)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                    } else {
                                        firestore.collection("Buyers").document(user.uid).get()
                                            .addOnSuccessListener { buyerDocument ->
                                                if (buyerDocument.exists()) {
                                                    val intent = Intent(this, BuyerHomeActivity::class.java)
                                                    intent.putExtra("USER_ID", user.uid)
                                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    startActivity(intent)
                                                } else {
                                                    Toast.makeText(
                                                        baseContext,
                                                        "User data not found.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        forgotPassword.setOnClickListener {
            // ... (your existing forgot password code)
        }

        register.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}

