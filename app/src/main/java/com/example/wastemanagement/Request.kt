package com.example.wastemanagement

data class Request(
    val documentId: String ="",
    val buyerId: String = "",
    val sellerId: String = "",
    var request: String = "",
    var status: String = "",
    var weight: Double = 0.0,
    var selectedWasteType: String = "",
)
