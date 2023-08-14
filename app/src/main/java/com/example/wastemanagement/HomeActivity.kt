package com.example.wastemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val userName = intent.getStringExtra("USER")
        val textView = findViewById<TextView>(R.id.hiname)
        textView.text = "Hi $userName!"
    }
}