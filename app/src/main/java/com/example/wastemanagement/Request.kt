package com.example.wastemanagement

data class Request(
    val documentId: String ="",
    val buyerId: String = "",
    val sellerId: String = "",
    var status: String = ""
)
