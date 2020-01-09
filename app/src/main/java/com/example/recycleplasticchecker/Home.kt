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


    private lateinit var mAuth : FirebaseAuth
    lateinit var navMenu: Menu
    lateinit var navigationView: NavigationView


    //Check user logged in
    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        if (user != null) {
            if (!mAuth.currentUser!!.email.equals("")){
                navigationView = activity!!.findViewById(R.id.navView)
                functionForLoggedIn()
            }
            else{
                functionForAnonymous()
            }
        } else {
            functionForAnonymous()
            signInAnonymously()
        }
    }

    override fun onPause() {
        super.onPause()
        val user = mAuth.currentUser
        if (user != null) {
            if (!mAuth.currentUser!!.email.equals("")){
                functionForLoggedIn()
            }
            else{
                functionForAnonymous()
            }
        } else {

            functionForAnonymous()
            signInAnonymously()
        }
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home,container,false)
        mAuth = FirebaseAuth.getInstance()


        binding.btCodeCheck.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_home_to_codeCheck)
        }

        binding.btQuiz.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_home_to_quizStart)
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

            //display message at nav header
            navigationView.findViewById<TextView>(R.id.usernameView).text = "Anonymous"
            navigationView.findViewById<TextView>(R.id.emailView).text = mAuth.currentUser?.uid
            Toast.makeText(activity,"loggged in Anonymous", Toast.LENGTH_SHORT)
        }
            .addOnFailureListener { exception -> Log.e(TAG, "signInAnonymously:FAILURE", exception) }
    }


    private fun functionForAnonymous(){

        //get navigation drawer and the menu
        navigationView = activity!!.findViewById(R.id.navView)
        //var navHeader = activity!!.findViewById<View>(R.id.navHeader)
        navMenu = navigationView.menu

        //disable logout and profile button
        navMenu.findItem(R.id.logout).isVisible = false
        activity!!.findViewById<Button>(R.id.btProfile).isEnabled = false
        activity!!.findViewById<Button>(R.id.btRedeem).isEnabled = false

        //enable login button and register button
        navMenu.findItem(R.id.login).isVisible = true
        navMenu.findItem(R.id.register).isVisible = true



    }

    private fun functionForLoggedIn(){

        //get navigation drawer and the menu
        navigationView = activity!!.findViewById(R.id.navView)
        navMenu = navigationView.menu


        //enable logout and profile button, redeem button
        navMenu.findItem(R.id.logout).isVisible = true
        activity!!.findViewById<Button>(R.id.btProfile).isEnabled = true
        activity!!.findViewById<Button>(R.id.btRedeem).isEnabled = true

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
