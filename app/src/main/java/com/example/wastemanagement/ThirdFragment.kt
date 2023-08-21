package com.example.wastemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ThirdFragment : Fragment() {

    private var userType: String = "Buyers"
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val userId: String? = auth.currentUser?.uid
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        val edit = view.findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        edit.setOnClickListener{
            isEditMode = !isEditMode
            toggleEditMode(view, edit, btnSave)
        }

        btnSave.setOnClickListener{
            if(isEditMode){
                saveChanges(view)
                isEditMode = false
                toggleEditMode(view, edit, btnSave)
            }
        }

        toggleEditMode(view, edit, btnSave)

        return view
    }

    private fun toggleEditMode(view: View, edit: FloatingActionButton, btnSave: Button) {
        val userName = view.findViewById<EditText>(R.id.editTextText)
        val userEmail = view.findViewById<EditText>(R.id.editTextTextEmailAddress2)
        val userPhone = view.findViewById<EditText>(R.id.editTextPhone2)

        userName.isEnabled = isEditMode
        userEmail.isEnabled = isEditMode
        userPhone.isEnabled = isEditMode

        // Toggle the visibility of the buttons based on the edit mode
        if (isEditMode) {
            edit.visibility = View.GONE
            btnSave.visibility = View.VISIBLE
        } else {
            edit.visibility = View.VISIBLE
            btnSave.visibility = View.GONE
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId?.let { uid ->
            fetchAndDisplayUserProfile("Buyers", uid, view)
            fetchAndDisplayUserProfile("Sellers", uid, view)
        }

        firestore.collection("Buyers").document(userId!!).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    userType = "Buyers"
                }
            }

        firestore.collection("Sellers").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    userType = "Sellers"
                }
            }
    }

    private fun fetchAndDisplayUserProfile(collectionName: String, userId: String, view: View) {
        val userName = view.findViewById<EditText>(R.id.editTextText)
        val userEmail = view.findViewById<EditText>(R.id.editTextTextEmailAddress2)
        val userPhone = view.findViewById<EditText>(R.id.editTextPhone2)
        firestore.collection(collectionName).document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val username = documentSnapshot.getString("Name")
                    val email = documentSnapshot.getString("Email")
                    val phoneNumber = documentSnapshot.getString("Phone")

                    userName.setText(username)
                    userEmail.setText(email)
                    userPhone.setText(phoneNumber)
                } else {
                    println("Document does not exist in $collectionName collection")
                }
            }
            .addOnFailureListener {
                println("Error fetching user profile from $collectionName collection")
            }
    }

    private fun saveChanges(view: View) {
        val newUsername = view.findViewById<EditText>(R.id.editTextText).text.toString()
        val newEmail = view.findViewById<EditText>(R.id.editTextTextEmailAddress2).text.toString()
        val newPhone = view.findViewById<EditText>(R.id.editTextPhone2).text.toString()

        // Update the Firestore document with new user details
        val collectionName = if (userType == "Buyers") "Buyers" else "Sellers"
        val userRef = firestore.collection(collectionName).document(userId!!)
        val data = hashMapOf(
            "Name" to newUsername,
            "Email" to newEmail,
            "Phone" to newPhone
        )
        userRef.set(data, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(context, "Changes saved successfully", Toast.LENGTH_SHORT).show()

                val editButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton2)
                val saveButton = view.findViewById<Button>(R.id.btnSave)
                editButton.visibility = View.VISIBLE
                saveButton.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to save changes", Toast.LENGTH_SHORT).show()
            }
    }
}
