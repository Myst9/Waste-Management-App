package com.example.wastemanagement


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment


class SellerRequestsFragment:Fragment(R.layout.fragment_seller_requests) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.newRequests2)

        button.setOnClickListener {
            val intent = Intent(requireContext(), WasteTypeActivity::class.java)
            startActivity(intent)
        }
    }
}