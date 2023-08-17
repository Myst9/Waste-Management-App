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

class HomeActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    private val homeFragment=FirstFragment();
    private val requestFragment=SecondFragment();
    private val profileFragment=ThirdFragment();
    private val settingsFragment=FourthFragment();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        loadFragment(FirstFragment())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(FirstFragment())
                    true
                }
                R.id.requests -> {
                    loadFragment(SecondFragment())
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