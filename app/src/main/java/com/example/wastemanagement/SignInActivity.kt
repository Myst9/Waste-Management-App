package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val signIn = findViewById<Button>(R.id.signin)
        signIn.setOnClickListener {
            var enteredEmail = email.text.toString()
            if(enteredEmail==""){
                Toast.makeText(
                    this@SignInActivity,
                    "Enter your email!",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("USER", "User")
                startActivity(intent)
            }
        }
    }
}