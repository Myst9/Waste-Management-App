package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val phoneNo = findViewById<EditText>(R.id.phoneNo)
        val sell = findViewById<RadioButton>(R.id.sellButton)
        val buy = findViewById<RadioButton>(R.id.buyButton)
        val signUp = findViewById<Button>(R.id.signup)
        val signInText = findViewById<TextView>(R.id.signinoption)
        var userName = ""
        signUp.setOnClickListener {
            userName = name.text.toString()
            if(userName==""){
                Toast.makeText(
                    this@MainActivity,
                    "Enter your name!",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("USER", userName)
                startActivity(intent)
            }
        }
        signInText.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}