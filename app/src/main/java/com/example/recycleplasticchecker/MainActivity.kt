package com.example.recycleplasticchecker

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.recycleplasticchecker.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


private const val TAG: String = "MainActivity"

class MainActivity : AppCompatActivity() {

    class User(
        val name: String,
        val email: String,
        val username: String,
        val password: String,
        val point: Int
    )

    lateinit var myDialog: Dialog
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mAuth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    lateinit var user: FirebaseUser
    lateinit var uid: String
    lateinit var point: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()

        //Log.d("Token", "" +FirebaseInstanceId.getInstance().getToken());

        println("onCreate MainAct")
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        myDialog = Dialog(this)
    }

    lateinit var tv_close: TextView
    lateinit var btnRedeem: Button
    lateinit var redeemLazada: TextView
    lateinit var redeemShopee: TextView
    lateinit var redeemZalora: TextView
    lateinit var redeemEbay: TextView

    fun ShowPopup1(v: View) {

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
            redeemLazada.isGone = false
        }
        myDialog.show()

    }

    fun ShowPopup2(v: View) {

        myDialog.setContentView(R.layout.popup2)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)
        redeemShopee = myDialog.findViewById(R.id.ShopeeCode)
        redeemShopee.isGone = true
        tv_close.setOnClickListener(object : View.OnClickListener {
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

    fun ShowPopup3(v: View) {

        myDialog.setContentView(R.layout.popup3)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)
        redeemZalora = myDialog.findViewById(R.id.ZaloraCode)
        redeemZalora.isGone = true
        tv_close.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                myDialog.dismiss()
            }
        })
        btnRedeem.setOnClickListener {
            redeem()
            redeemZalora.isGone = false
        }
        myDialog.show()
    }

    fun ShowPopup4(v: View) {

        myDialog.setContentView(R.layout.popup4)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)
        redeemEbay = myDialog.findViewById(R.id.EbayCode)
        redeemEbay.isGone = true
        tv_close.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                myDialog.dismiss()
            }
        })
        btnRedeem.setOnClickListener {
            redeem()
            redeemEbay.isGone = false
        }
        myDialog.show()
    }

    fun redeem() {
        var count = 1
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

                println("hahahaha: " + user_point)

                if (user_point.toInt() >= 1000 && count == 1) {
                    try {
                        println("hahahahha : " +count)
                        val currentPoint = user_point.toInt() - 1000
                        val user = User(user_name, user_email, user_username, user_password, currentPoint)
                        database.setValue(user)
                        Toast.makeText(this@MainActivity, "Redeem Successfully!!", Toast.LENGTH_SHORT).show()
                        count--
                    } catch (ex: NumberFormatException) {
                    }

                }
                else if(count == 0) {
                    Toast.makeText(this@MainActivity, "Redeem Successfully!!", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@MainActivity, "Your point is insufficient!!", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}
