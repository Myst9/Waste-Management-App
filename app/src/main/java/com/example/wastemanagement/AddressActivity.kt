package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        val button = findViewById<Button>(R.id.confirm)

        button.setOnClickListener {
            val doorNo = findViewById<EditText>(R.id.doorNumber).text.toString()
            val street = findViewById<EditText>(R.id.street).text.toString()
            val pinCode = findViewById<EditText>(R.id.pincode).text.toString()
            val city = findViewById<EditText>(R.id.city).text.toString()
            val state = findViewById<EditText>(R.id.state).text.toString()

            if (doorNo.isEmpty() || street.isEmpty() || pinCode.isEmpty() || city.isEmpty() || state.isEmpty()) {
                Toast.makeText(
                    this@AddressActivity,
                    "Please fill all the fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
                else{
                    val intent = Intent(this, BuyerHomeActivity::class.java)
                    startActivity(intent)
                }

        }
    }
}