package com.example.wastemanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SellerHomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser
        loadUserName(userId,view)


        // ... (other code)
    }

    private fun loadUserName(userId: FirebaseUser?, view: View) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("Sellers").document(userId?.uid ?: "")

        val textViewGreeting = view.findViewById<TextView>(R.id.textViewGreeting)

        userRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val userName = documentSnapshot.getString("Name")
                if (!userName.isNullOrEmpty()) {
                    val greeting = "Hi $userName!"
                    Log.d("SellerHomeFragment", "Greeting: $greeting")

                    // Update UI on the main thread
                    activity?.runOnUiThread {
                        textViewGreeting.text = greeting
                    }
                }
            } else {
                // Handle the case when the document doesn't exist
            }
        }
        val sendRequestButton = view.findViewById<Button>(R.id.buttonSendRequest)

        // Set a click listener for the button
        sendRequestButton.setOnClickListener {
            // Create an Intent to navigate to the WasteTypeActivity
            val intent = Intent(activity, WasteTypeActivity::class.java)
            startActivity(intent)
        }
    }




    // ... (other methods)
}
