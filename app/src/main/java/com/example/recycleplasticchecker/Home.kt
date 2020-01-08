package com.example.recycleplasticchecker


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.recycleplasticchecker.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment() {

    lateinit var navMenu: Menu
    lateinit var navigationView: NavigationView

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home, container, false
        )

        navigationView = activity!!.findViewById(R.id.navView)
        navMenu = navigationView.menu
        //disable logout if no user logged in
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            navMenu.findItem(R.id.logout).isVisible = false
            navMenu.findItem(R.id.login).isVisible = true
            navMenu.findItem(R.id.register).isVisible = true
            activity!!.findViewById<View>(R.id.profile).isEnabled = false
        }
        else{
            navMenu.findItem(R.id.login).isVisible = false
            navMenu.findItem(R.id.register).isVisible = false
        }

        binding.btCodeCheck.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_home_to_codeCheck)
        }

        binding.btQuiz.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_home_to_quizStart)
        binding.btQuiz.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_home_to_quiz)
        }

        binding.btRedeem.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_home_to_redeem)
        }

        binding.btProfile.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_home_to_profile)
        }

        binding.btLocation.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_home_to_recycleBinLocation)
        }

        binding.btPlasticRecycleCheck.setOnClickListener { view: View ->
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
            view!!.findNavController()
        ) || super.onOptionsItemSelected(item)
    }

}
