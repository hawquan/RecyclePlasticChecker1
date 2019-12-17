package com.example.recycleplasticchecker


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.recycleplasticchecker.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home,container,false)

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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

}
