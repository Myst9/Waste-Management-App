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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SellerHomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seller_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load user's name and update greeting
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser
        loadUserName(userId, view)

        // Initialize RecyclerView and Adapter
        recyclerView = view.findViewById(R.id.recyclerView)
        val itemList = generateSampleItems()
        adapter = MyAdapter(itemList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set a click listener for the button
        val sendRequestButton = view.findViewById<Button>(R.id.buttonSendRequest)
        sendRequestButton.setOnClickListener {
            val intent = Intent(activity, WasteTypeActivity::class.java)
            startActivity(intent)
        }
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
                    textViewGreeting.text = greeting
                }
            }
        }
    }

    private fun generateSampleItems(): List<Item> {
        return listOf(
            Item("Recycling a kilogram of plastic can reduce carbondioxide equivalent emissions by  37%", R.drawable.co2),
            Item("Recycling a kilogram of paper can reduce carbondioxide equivalent emissions by  37%", R.drawable.paper1),
            Item("Recycling a kilogram of glass can reduce carbondioxide equivalent emissions by  41%", R.drawable.glass),
            Item("Recycling a ton of plastic saves 5,774 kWh of energy", R.drawable.plastic),
            Item("Recycling a ton of paper saves 5,774 kWh of energy", R.drawable.tree),
            Item("Recycling a ton of paper saves 4,100 kWh of energy", R.drawable.bulb),
            Item("Recycling a ton of glass saves  kWh of energy", R.drawable.glassimg),
        )
    }
}