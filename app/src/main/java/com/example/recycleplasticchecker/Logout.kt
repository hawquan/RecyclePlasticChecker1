package com.example.recycleplasticchecker


import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class Logout : Fragment() {

    lateinit var navMenu: Menu
    lateinit var navigationView: NavigationView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseAuth.getInstance().signOut()

        //get navigation drawer and the menu
        navigationView = activity!!.findViewById(R.id.navView)
        navMenu = navigationView.menu


        //disable logout and profile button
        navMenu.findItem(R.id.logout).isVisible = false
        //profileBtn.isEnabled = false
        activity!!.findViewById<Button>(R.id.btProfile).isEnabled = false

        //enable login button and register button
        navMenu.findItem(R.id.login).isVisible = true
        navMenu.findItem(R.id.register).isVisible = true

        fragmentManager!!.popBackStack()
        //back to home page
        view!!.findNavController().navigate(R.id.action_logout_to_home)

    }


}
