package com.example.recycleplasticchecker


import android.content.ClipData
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 */

class  Login : Fragment() {

    class Account(val name: String = "", val email: String = "", val username: String = "", val password: String = "")

    lateinit var rellay1:RelativeLayout
    lateinit var rellay2:RelativeLayout
    lateinit var editUsername: EditText
    lateinit var editPassword: EditText
    lateinit var btnLogin: Button
    lateinit var linkRegisterPage: TextView
    lateinit var linkForgetPassword: TextView
    lateinit var mAuth : FirebaseAuth
    lateinit var navMenu : Menu
    lateinit var navigationView : NavigationView
    lateinit var btnProfile : Button
    lateinit var btnRedeem : Button


    var handler : Handler = Handler()
    var runnable : Runnable = object  : Runnable{
        override  fun run(){
            rellay1.visibility = View.VISIBLE
            rellay2.visibility = View.VISIBLE
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rellay1 = activity!!.findViewById(R.id.rellay1)
        rellay2 = activity!!.findViewById(R.id.rellay2)

        handler.postDelayed(runnable, 1250)

        editUsername = activity!!.findViewById(R.id.editUsername)
        editPassword= activity!!.findViewById(R.id.editPassword)
        btnLogin = activity!!.findViewById(R.id.btnLogin)
        linkRegisterPage = activity!!.findViewById(R.id.linkRegisterPage)
        linkForgetPassword = activity!!.findViewById(R.id.linkForgetPassword)


        //to hide item in navigation once logged in
        navigationView = activity!!.findViewById(R.id.navView)
        navMenu = navigationView.menu
        btnProfile = activity!!.findViewById(R.id.btProfile)
        btnRedeem = activity!!.findViewById(R.id.btRedeem)

        btnLogin.setOnClickListener() {
            login()
        }

        linkRegisterPage.setOnClickListener {
            view.findNavController().navigate(R.id.action_login_to_register)
        }

        linkForgetPassword.setOnClickListener{
            view.findNavController().navigate(R.id.action_login_to_forgetPassword)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun login() {
        val username = editUsername.text.toString().trim()
        val password = editPassword.text.toString().trim()

        if (username.isEmpty() && password.isEmpty()) {
            editUsername.error = "Please enter username"
            editPassword.error = "Please enter password"
            return
        }


        if(!isEmailValid(username)) {
            //Login with username and password
            val database = FirebaseDatabase.getInstance().reference

            database.child("Users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (h in dataSnapshot.children) {
                        val account = h.getValue(Account::class.java)
                        val username1 = account!!.username
                        val password1 = account.password
                        val email1 = account.email

                        if (username.equals(username1) && password.equals(password1)) {
                            //mAuth.signInWithEmailAndPassword(email1, password1)
                            functionForLoggedIn()
                            navigationView.findViewById<TextView>(R.id.usernameView).text = username
                            navigationView.findViewById<TextView>(R.id.emailView).text = email1
                            Toast.makeText(activity, "Login Successfully", Toast.LENGTH_SHORT).show()
                            view!!.findNavController().navigate(R.id.action_login_to_home)
                        } else {
                            Toast.makeText(activity, "Invalid Username or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    throw databaseError.toException()
                }
            })
        }else{
            //Login with email and password, firebase authentication
            LoginWithAuth(username,password)
        }

    }

    private fun LoginWithAuth(email: String, password:String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(object: OnCompleteListener<AuthResult>{
            override fun onComplete(task: Task<AuthResult>) {
                if(task.isSuccessful){
                    functionForLoggedIn()
                    navigationView.findViewById<TextView>(R.id.usernameView).text = "Username"
                    navigationView.findViewById<TextView>(R.id.emailView).text = email
                    Toast.makeText(activity, "Login Successfully", Toast.LENGTH_SHORT).show()
                    view!!.findNavController().navigate(R.id.action_login_to_home)
                }else{
                    Toast.makeText(activity, "Invalid Username or password", Toast.LENGTH_SHORT).show()
                }
            }
    })
    }

    private fun functionForLoggedIn(){

        //get navigation drawer and the menu
        navigationView = activity!!.findViewById(R.id.navView)
        navMenu = navigationView.menu


        //enable logout and profile button, redeem button
        navMenu.findItem(R.id.logout).isVisible = true
        btnProfile.isEnabled = true
        btnRedeem.isEnabled = true

        //disable login button and register button
        navMenu.findItem(R.id.login).isVisible = false
        navMenu.findItem(R.id.register).isVisible = false
    }

    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

}




