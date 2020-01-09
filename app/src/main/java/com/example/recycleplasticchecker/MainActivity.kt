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
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.popup1.*


private const val TAG : String = "MainActivity"

class MainActivity : AppCompatActivity() {

    class User(val name: String, val email: String, val username: String, val password: String, val point: Int)

    lateinit var myDialog: Dialog
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mAuth : FirebaseAuth
    lateinit var databaseReference : DatabaseReference
    lateinit var user : FirebaseUser
    lateinit var uid : String
    lateinit var point:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()

        //Log.d("Token", "" +FirebaseInstanceId.getInstance().getToken());

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
    lateinit var  redeemLazada:TextView
    lateinit var redeemShopee:TextView
    lateinit var  redeemZalora:TextView
    lateinit var  redeemEbay:TextView

    fun ShowPopup1(v:View) {

        myDialog.setContentView(R.layout.popup1)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)
        redeemLazada = myDialog.findViewById(R.id.lazadaCode)
        redeemLazada.isGone = true

        tv_close.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                myDialog.dismiss()

            }
        })
        btnRedeem.setOnClickListener {
            redeem()
            redeemLazada.isGone=false
        }
        myDialog.show()

    }

        fun ShowPopup2(v:View){

        myDialog.setContentView(R.layout.popup2)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)
        redeemShopee = myDialog.findViewById(R.id.ShopeeCode)
        redeemShopee.isGone=true
        tv_close.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                myDialog.dismiss()
            }
        })
        btnRedeem.setOnClickListener {
            redeem()
            redeemShopee.isGone = false
        }
            myDialog.show()

    }

    fun ShowPopup3(v:View){

        myDialog.setContentView(R.layout.popup3)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)
        redeemZalora=myDialog.findViewById(R.id.ZaloraCode)
        redeemZalora.isGone=true
        tv_close.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                myDialog.dismiss()
            }
        })
        btnRedeem.setOnClickListener {
            redeem()
            redeemZalora.isGone=false
        }
        myDialog.show()
    }

    fun ShowPopup4(v:View){

        myDialog.setContentView(R.layout.popup4)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)
        redeemEbay= myDialog.findViewById(R.id.EbayCode)
        redeemEbay.isGone=true
        tv_close.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                myDialog.dismiss()
            }
        })
        btnRedeem.setOnClickListener {
            redeem()
            redeemEbay.isGone=false
        }
        myDialog.show()
    }

     fun redeem(){
        user = FirebaseAuth.getInstance().currentUser!!
        uid = user.uid
        val database = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user_name = dataSnapshot.child("name").value.toString()
                var user_email = dataSnapshot.child("email").value.toString()
                var user_username = dataSnapshot.child("username").value.toString()
                var user_password = dataSnapshot.child("password").value.toString()
                var user_point = dataSnapshot.child("point").value.toString()

                if(user_point >= 1000.toString()){
                    val currentPoint=  user_point.toInt() - 1000
                    val user = User(user_name, user_email, user_username, user_password, currentPoint)
                    database.setValue(user)
                }
                else{
                    Toast.makeText(this@MainActivity,"Your point is insufficient!!",Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}
