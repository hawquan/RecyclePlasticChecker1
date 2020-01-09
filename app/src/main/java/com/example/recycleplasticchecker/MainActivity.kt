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
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.popup1.*


private const val TAG : String = "MainActivity"

class MainActivity : AppCompatActivity() {

    class User(val name: String = "", val email: String = "", val username: String = "", val password: String = "", val point: Int)

    lateinit var myDialog: Dialog
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mAuth : FirebaseAuth




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
    fun ShowPopup1(v:View){

        myDialog.setContentView(R.layout.popup1)
        tv_close = myDialog.findViewById(R.id.tv_cancel)
        btnRedeem = myDialog.findViewById(R.id.btnRedeem)
        redeemLazada=myDialog.findViewById(R.id.lazadaCode)
        redeemLazada.isGone=true
        //redeemLazada.visibility=gone
        tv_close.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                myDialog.dismiss()

            }
        })
        btnRedeem.setOnClickListener {
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
            redeemShopee.isGone=false
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
            redeemEbay.isGone=false
        }
        myDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}
