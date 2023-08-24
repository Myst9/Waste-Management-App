package com.example.wastemanagement

import android.content.Intent
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

class UserProfileFragment : Fragment() {

    private lateinit var userType: String
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val userId: String? = auth.currentUser?.uid
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
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
        fetchAndDisplayUserProfile()

        val btnSelectWasteTypes = view.findViewById<Button>(R.id.btnSelectWasteTypes)
        btnSelectWasteTypes.setOnClickListener {
            val intent = Intent(requireContext(), BuyerActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun toggleEditMode(view: View, edit: FloatingActionButton, btnSave: Button) {
        val userName = view.findViewById<EditText>(R.id.editTextText)
        val userEmail = view.findViewById<EditText>(R.id.editTextTextEmailAddress2)
        val userPhone = view.findViewById<EditText>(R.id.editTextPhone2)
        val addressEditText = view.findViewById<EditText>(R.id.editTextTextPostalAddress)

        val btnSelectWasteTypes = view.findViewById<Button>(R.id.btnSelectWasteTypes)
        btnSelectWasteTypes.visibility = if (isEditMode && userType == "Buyers") View.VISIBLE else View.GONE

        // Toggle the visibility of the Save button
        btnSave.visibility = if (isEditMode) View.VISIBLE else View.GONE

        userName.isEnabled = isEditMode
        userEmail.isEnabled = isEditMode
        userPhone.isEnabled = isEditMode
        addressEditText.isEnabled = isEditMode

        if (isEditMode && userType == "Buyers") {
            btnSelectWasteTypes.visibility = View.VISIBLE
        } else {
            btnSelectWasteTypes.visibility = View.GONE
        }
    }

    private fun fetchAndDisplayUserProfile() {
        userId?.let { uid ->
            firestore.collection("Buyers").document(uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        userType = "Buyers"
                        val username = documentSnapshot.getString("Name")
                        val email = documentSnapshot.getString("Email")
                        val phoneNumber = documentSnapshot.getString("Phone")

                        val userName = requireView().findViewById<EditText>(R.id.editTextText)
                        val userEmail = requireView().findViewById<EditText>(R.id.editTextTextEmailAddress2)
                        val userPhone = requireView().findViewById<EditText>(R.id.editTextPhone2)

                        userName.setText(username)
                        userEmail.setText(email)
                        userPhone.setText(phoneNumber)

                        val addressLine1 = documentSnapshot.getString("AddressLine1")
                        val addressLine2 = documentSnapshot.getString("AddressLine2")
                        val pincode = documentSnapshot.getString("Pincode")
                        val city = documentSnapshot.getString("City")

                        val addressMap = mapOf(
                            "AddressLine1" to addressLine1,
                            "AddressLine2" to addressLine2,
                            "Pincode" to pincode,
                            "City" to city
                        )

                        displayAddress(addressMap)
                    } else {
                        println("Document does not exist in Buyers collection")
                    }
                }
                .addOnFailureListener {
                    println("Error fetching user profile from Buyers collection")
                }

            firestore.collection("Sellers").document(uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        userType = "Sellers"
                        val username = documentSnapshot.getString("Name")
                        val email = documentSnapshot.getString("Email")
                        val phoneNumber = documentSnapshot.getString("Phone")

                        val userName = requireView().findViewById<EditText>(R.id.editTextText)
                        val userEmail = requireView().findViewById<EditText>(R.id.editTextTextEmailAddress2)
                        val userPhone = requireView().findViewById<EditText>(R.id.editTextPhone2)
                        userName.setText(username)
                        userEmail.setText(email)
                        userPhone.setText(phoneNumber)

                        val addressLine1 = documentSnapshot.getString("AddressLine1")
                        val addressLine2 = documentSnapshot.getString("AddressLine2")
                        val pincode = documentSnapshot.getString("Pincode")
                        val city = documentSnapshot.getString("City")

                        val addressMap = mapOf(
                            "AddressLine1" to addressLine1,
                            "AddressLine2" to addressLine2,
                            "Pincode" to pincode,
                            "City" to city
                        )
                        displayAddress(addressMap)
                        userName.setText(username)
                        userEmail.setText(email)
                        userPhone.setText(phoneNumber)
                        // Fetch and display seller's profile information here
                    } else {
                        println("Document does not exist in Sellers collection")
                    }
                }
                .addOnFailureListener {
                    println("Error fetching user profile from Sellers collection")
                }
        }
    }

    private fun displayAddress(addressMap: Map<*, *>?) {
        if (addressMap != null) {
            val doorNo = addressMap["AddressLine1"] as? String
            val street = addressMap["AddressLine2"] as? String
            val city = addressMap["City"] as? String
            val pinCode = addressMap["Pincode"] as? String

            val addressEditText = requireView().findViewById<EditText>(R.id.editTextTextPostalAddress)
            val formattedAddress = "$doorNo, $street, $city, $pinCode"
            addressEditText.setText(formattedAddress)
        }
    }


    private fun saveChanges(view: View) {
        val newUsername = view.findViewById<EditText>(R.id.editTextText).text.toString()
        val newEmail = view.findViewById<EditText>(R.id.editTextTextEmailAddress2).text.toString()
        val newPhone = view.findViewById<EditText>(R.id.editTextPhone2).text.toString()
        val newAddress = view.findViewById<EditText>(R.id.editTextTextPostalAddress).text.toString()

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
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to save changes", Toast.LENGTH_SHORT).show()
            }
    }
}