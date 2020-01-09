package com.example.recycleplasticchecker


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.recycleplasticchecker.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
private const val TAG : String = "Home"


/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mAuth : FirebaseAuth
    lateinit var navMenu: Menu
    lateinit var navigationView: NavigationView
    private var isSignInAnonymously : Boolean = false


    //Check user logged in
    override fun onStart() {
        super.onStart()
        println("Sign In A :" +isSignInAnonymously)
        println("onStart Home")
        val user = mAuth.currentUser
        if (user != null) {
            if (!isSignInAnonymously){
                println("user ada")
                functionForLoggedIn()
            }
            else{
                println("user ada but anonymous")
                functionForAnonymous()
            }
        } else {
            println("user x ada")
            functionForAnonymous()
            signInAnonymously()
        }
    }

    override fun onPause() {
        super.onPause()
        println("onPause Home")
        val user = mAuth.currentUser
        if (user != null) {
            if (!isSignInAnonymously){
                println("user ada")
                functionForLoggedIn()
            }
            else{
                println("user ada but anonymous")
            }
        } else {
            println("user x ada")
            functionForAnonymous()
            signInAnonymously()
        }
    }

    override fun onCreateView(


        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        println("onCreateView Home")
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home,container,false)
        mAuth = FirebaseAuth.getInstance()
        binding.btCodeCheck.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_home_to_codeCheck)
        }

        binding.btQuiz.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_home_to_quiz)
        }

        binding.btRedeem.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_home_to_redeem)
        }

        binding.btProfile.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_home_to_profile)
        }

        binding.btLocation.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_home_to_recycleBinLocation)
        }

        binding.btPlasticRecycleCheck.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_home_to_plasticRecycleCheck)
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    private fun signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener() {

            isSignInAnonymously = true
            //display message at nav header
            Toast.makeText(activity,"loggged in Anonymous", Toast.LENGTH_SHORT)
        }
            .addOnFailureListener { exception -> Log.e(TAG, "signInAnonymously:FAILURE", exception) }
    }


    private fun functionForAnonymous(){

        //get navigation drawer and the menu
        navigationView = activity!!.findViewById(R.id.navView)
        var navHeader = activity!!.findViewById<View>(R.id.navHeader)
        navMenu = navigationView.menu

        //disable logout and profile button
        navMenu.findItem(R.id.logout).isVisible = false
        //profileBtn.isEnabled = false
        activity!!.findViewById<Button>(R.id.btProfile).isEnabled = false

        //enable login button and register button
        navMenu.findItem(R.id.login).isVisible = true
        navMenu.findItem(R.id.register).isVisible = true


    }

    private fun functionForLoggedIn(){

        //get navigation drawer and the menu
        navigationView = activity!!.findViewById(R.id.navView)
        navMenu = navigationView.menu


        //enable logout and profile button
        navMenu.findItem(R.id.logout).isVisible = true
        //profileBtn.isEnabled = false
        //this.findViewById<Button>(R.id.btProfile).isEnabled = true

        //disable login button and register button
        navMenu.findItem(R.id.login).isVisible = false
        navMenu.findItem(R.id.register).isVisible = false
    }


    private fun getLoggedInUserDetails() : MainActivity.User {

        lateinit var user : MainActivity.User
        var databaseReference : DatabaseReference = FirebaseDatabase.getInstance().reference


        databaseReference.child("Users").child(mAuth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {


            override fun onCancelled(databaseError: DatabaseError) {
                /*Toast.makeText(
                    MainActivity.this,
                    "Network ERROR. Please check your connection",
                    Toast.LENGTH_SHORT
                ).show()*/
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //ensure it is not anonymous
                if(dataSnapshot.hasChild("email")){
                    //sure this is not null add!!
                    val userDetail = dataSnapshot.getValue(MainActivity.User::class.java)!!
                    user = MainActivity.User(
                        userDetail.name,
                        userDetail.email,
                        userDetail.username,
                        userDetail.password,
                        userDetail.point
                    )
                }
            }
        })

        return user
    }

}
