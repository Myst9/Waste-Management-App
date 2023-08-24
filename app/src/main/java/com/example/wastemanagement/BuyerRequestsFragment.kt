package com.example.wastemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BuyerRequestsFragment : Fragment(R.layout.fragment_buyer_requests) {
    private lateinit var activeRecyclerView: RecyclerView
    private lateinit var archivedRecyclerView: RecyclerView
    private val firestore = FirebaseFirestore.getInstance()
    private val requestsCollection = firestore.collection("requests")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buyer_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activeRecyclerView = view.findViewById(R.id.activeRecyclerView)
        archivedRecyclerView = view.findViewById(R.id.archivedRecyclerView)

        val activeAdapter = RequestAdapter()
        val archivedAdapter = RequestAdapter()

        setupRecyclerView(activeRecyclerView, activeAdapter)
        setupRecyclerView(archivedRecyclerView, archivedAdapter)

        loadRequests(activeAdapter, archivedAdapter)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RequestAdapter) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun loadRequests(
        activeAdapter: RequestAdapter,
        archivedAdapter: RequestAdapter
    ) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return

        val activeRequestsQuery = requestsCollection
            .whereEqualTo("buyerId", userId)
            .whereIn("status", listOf("accepted"))

        val archivedRequestsQuery = requestsCollection
            .whereEqualTo("buyerId", userId)
            .whereIn("status", listOf("rejected", "completed"))

        activeRequestsQuery.get().addOnCompleteListener { activeRequestsTask ->
            if (activeRequestsTask.isSuccessful) {
                val activeRequests = activeRequestsTask.result?.toObjects(Request::class.java) ?: emptyList()
                activeAdapter.submitList(activeRequests)
            }
        }

        archivedRequestsQuery.get().addOnCompleteListener { archivedRequestsTask ->
            if (archivedRequestsTask.isSuccessful) {
                val archivedRequests = archivedRequestsTask.result?.toObjects(Request::class.java) ?: emptyList()
                archivedAdapter.submitList(archivedRequests)
            }
        }
    }
}

