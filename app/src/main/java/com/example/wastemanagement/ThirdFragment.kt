package com.example.wastemanagement
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import java.util.zip.Inflater

class ThirdFragment:Fragment() {

    private  val auth = FirebaseAuth.getInstance()
    private  val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        // Initialize your UI components here using the inflated view
        val userName = view.findViewById<EditText>(R.id.editTextText)
        val userEmail = view.findViewById<EditText>(R.id.editTextTextEmailAddress2)
        val userPhone = view.findViewById<EditText>(R.id.editTextPhone2)

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            // Retrieve user details from Firestore
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val username = documentSnapshot.getString("name")
                        val email = documentSnapshot.getString("email")
                        val phoneNumber = documentSnapshot.getString("phoneNumber")

                        // Populate UI elements with user details
                        userName.setText(username)
                        userEmail.setText(email)
                        userPhone.setText(phoneNumber)
                    }
                }
        }
        return view
    }


}

