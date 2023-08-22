package com.example.wastemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class SellerHomeActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    private val homeFragment=SellerHomeFragment();
    private val requestFragment=SellerRequestsFragment();
    private val profileFragment=UserProfileFragment();
    private val settingsFragment=UserSettingsFragment();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_home)
        loadFragment(SellerHomeFragment())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(SellerHomeFragment())
                    true
                }
                R.id.requests -> {
                    loadFragment(SellerRequestsFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(UserProfileFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(UserSettingsFragment())
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