package com.example.wastemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

class HomeActivity2 : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    private val homeFragment=FirstFragment();
    private val requestFragment=SecondFragment();
    private val profileFragment=ThirdFragment();
    private val settingsFragment=FourthFragment();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        loadFragment(BuyerHomeFragment())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(BuyerHomeFragment())
                    true
                }
                R.id.requests -> {
                    loadFragment(RequestsFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ThirdFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(FourthFragment())
                    true
                }

                else -> {false}
            }
        }
    }
    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

}