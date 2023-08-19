package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class BuyerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer)
        val button = findViewById<Button>(R.id.next)

        button.setOnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
        }
    }

}