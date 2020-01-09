package com.example.recycleplasticchecker

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.recycleplasticchecker.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId


private const val TAG : String = "MainActivity"

class MainActivity : AppCompatActivity() {

    class User(val name: String = "", val email: String = "", val username: String = "", val password: String = "", val point: Int)

    lateinit var myDialog: Dialog
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mAuth : FirebaseAuth
    lateinit var navMenu: Menu
    lateinit var navigationView: NavigationView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()

        Log.d("Token", "" +FirebaseInstanceId.getInstance().getToken());

        println("onCreate MainAct")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        myDialog = Dialog(this)
    }

    lateinit  var tv_close:TextView
    lateinit  var btnRedeem:Button

    fun ShowPopup1(v:View){

        myDialog.setContentView(R.layout.popup1)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)

        tv_close.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                myDialog.dismiss()
            }
        })
        myDialog.show()
    }

    fun ShowPopup2(v:View){

        myDialog.setContentView(R.layout.popup2)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)

        tv_close.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                myDialog.dismiss()
            }
        })
        myDialog.show()
    }

    fun ShowPopup3(v:View){

        myDialog.setContentView(R.layout.popup3)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)

        tv_close.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                myDialog.dismiss()
            }
        })
        myDialog.show()
    }

    fun ShowPopup4(v:View){

        myDialog.setContentView(R.layout.popup4)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)

        tv_close.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                myDialog.dismiss()
            }
        })
        myDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}
