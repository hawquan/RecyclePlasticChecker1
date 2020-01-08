package com.example.recycleplasticchecker.quiz_won_lose


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.example.recycleplasticchecker.R
import com.example.recycleplasticchecker.databinding.FragmentQuizLoseBinding
import kotlinx.android.synthetic.main.fragment_quiz_lose.*

/**
 * A simple [Fragment] subclass.
 */
class QuizLose : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding= DataBindingUtil.inflate<FragmentQuizLoseBinding>(inflater,
            R.layout.fragment_quiz_lose,container,false)
        binding.LoseBackHomeButton.setOnClickListener {view: View ->
            view.findNavController().navigate(R.id.action_quizLose_to_home)
        }
        binding.TryAgainButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_quizLose_to_quiz)
        }
        return binding.root
    }


}
