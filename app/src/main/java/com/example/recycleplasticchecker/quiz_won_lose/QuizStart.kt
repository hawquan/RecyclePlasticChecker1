package com.example.recycleplasticchecker.quiz_won_lose


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.example.recycleplasticchecker.R
import com.example.recycleplasticchecker.databinding.FragmentHomeBinding
import com.example.recycleplasticchecker.databinding.FragmentQuizStartBinding
import kotlinx.android.synthetic.main.fragment_quiz_start.*

/**
 * A simple [Fragment] subclass.
 */
class QuizStart : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding= DataBindingUtil.inflate<FragmentQuizStartBinding>(inflater,
            R.layout.fragment_quiz_start,container,false)
        binding.QuizButton.setOnClickListener{view: View ->
            view.findNavController().navigate(R.id.action_quizStart_to_quiz)}
        return binding.root
    }


}
