package com.example.wastemanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class BuyerHomeFragment : Fragment() {
    private lateinit var newRequestRecyclerView: RecyclerView
    private val firestore = FirebaseFirestore.getInstance()
    private val requestsCollection = firestore.collection("requests")


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buyer_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser
        loadUserName(userId,view)
        super.onViewCreated(view, savedInstanceState)

        newRequestRecyclerView = view.findViewById(R.id.newRequestRecyclerView)

        val newRequestAdapter = RequestAdapterBuyer()

        setupRecyclerView(newRequestRecyclerView, newRequestAdapter)

        loadRequests(newRequestAdapter)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RequestAdapterBuyer) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
    private fun loadUserName(userId: FirebaseUser?, view: View) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("Buyers").document(userId?.uid ?: "")

        val textViewGreeting = view.findViewById<TextView>(R.id.textViewGreeting)

        userRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val userName = documentSnapshot.getString("Name")
                if (!userName.isNullOrEmpty()) {
                    val greeting = "Hi $userName!"
                    Log.d("BuyerHomeFragment", "Greeting: $greeting")

                    // Update UI on the main thread
                    activity?.runOnUiThread {
                        textViewGreeting.text = greeting
                    }
                }
            }
        }

    }

    private fun loadRequests(

        newRequestAdapter: RequestAdapterBuyer
    ) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return


        val newRequestsQuery = requestsCollection
            .whereEqualTo("buyerId", userId)
            .whereIn("status", listOf("pending"))


        newRequestsQuery.get().addOnCompleteListener { newRequestsTask ->
            if (newRequestsTask.isSuccessful) {
                val newRequests = newRequestsTask.result?.toObjects(Request::class.java) ?: emptyList()
                newRequestAdapter.submitList(newRequests)
            }
        }
    }
}