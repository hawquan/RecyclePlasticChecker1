package com.example.recycleplasticchecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.recycleplasticchecker.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.view.Menu
import android.view.View
import com.google.android.material.navigation.NavigationView

private const val TAG : String = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mAuth : FirebaseAuth
    lateinit var navMenu: Menu
    lateinit var navigationView: NavigationView

    override fun onStart() {
        super.onStart()

        val user = mAuth.getCurrentUser()
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance()

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        navigationView = this.findViewById(R.id.navView)
        navMenu = navigationView.menu
        //disable logout if no user logged in
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            navMenu.findItem(R.id.logout).isVisible = false
            navMenu.findItem(R.id.login).isVisible = true
            navMenu.findItem(R.id.register).isVisible = true
            this.findViewById<View>(R.id.profile).isEnabled = false
        }
        else{
            navMenu.findItem(R.id.login).isVisible = false
            navMenu.findItem(R.id.register).isVisible = false
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    private fun signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this) {
            // do your stuff
        }
            .addOnFailureListener(
                this
            ) { exception -> Log.e(TAG, "signInAnonymously:FAILURE", exception) }
    }
}
