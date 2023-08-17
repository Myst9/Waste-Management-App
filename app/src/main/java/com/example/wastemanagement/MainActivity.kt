package com.example.wastemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
//        var userEmail = ""
//        var userNumber = ""
//        var userPassword = ""
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        fun checkButton(): String{
            val radioId = radioGroup.checkedRadioButtonId

            val radioButton = findViewById<RadioButton>(radioId)
            return radioButton.text.toString()
        }
        signUp.setOnClickListener { userName = name.text.toString()
//            userEmail = email.text.toString()
//            userNumber = phoneNo.text.toString()
//            userPassword = password.text.toString()
            if(userName==""){
                Toast.makeText(
                    this@MainActivity,
                    "Enter your name!",
                    Toast.LENGTH_SHORT
                ).show()}
//
//                if(userEmail==""){
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Enter your email!",
//                        Toast.LENGTH_SHORT
//                    ).show()}
//                    if(userNumber==""){
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Enter your phone number!",
//                            Toast.LENGTH_SHORT
//                        ).show()}
//                        if(userPassword==""){
//                            Toast.makeText(
//                                this@MainActivity,
//                                "Enter your Password!",
//                                Toast.LENGTH_SHORT
//                            ).show()}
            else if(checkButton()=="Sell Waste"){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            //            else if(checkButton()=="Buy Waste") {
//                        val intent = Intent(this, BuyActivity::class.java)
//                        startActivity(intent)
//                    }
        }
        signInText.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}