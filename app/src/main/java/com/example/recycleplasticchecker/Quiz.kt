package com.example.recycleplasticchecker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.recycleplasticchecker.databinding.FragmentQuiz1Binding


/**
 * A simple [Fragment] subclass.
 */
class Quiz : Fragment() {
    data class Question(
        val text: String,
        val answers: List<String>
    )

    private val questions: MutableList<Question> = mutableListOf(
        Question(
            text = "What item does not belong in the recycling bin",
            answers = listOf("Styrofoam Cup", "Plastic Milk Jug", "Plastic Soda Bottle", "Laundry Detergent Bottle")
        ),
        Question(
            text = "Which aluminum item is not recyclable?",
            answers = listOf("Pots and Pans", "Soda Cans", "Aluminum Foil", "Aluminum Pie Pans")
        ),
        Question(
            text = "When is national Recycles Date?",
            answers = listOf("November 15", "December 25", "July 4", "February 14")
        ),
        Question(
            text = "Who should you call for more information on recycling?",
            answers = listOf("Ministry of Health Malaysia", "Fire Department", "JPJ","Police Department")
        ),
        Question(
            text = "What does the three R's of recycling mean?",
            answers = listOf(
                "Reduce Reuse Recycle ",
                "Reading writing and arithmetic",
                "nothing",
                "Reflection Relationships Risk-Taking"
            )
        ),
        Question(
            text = "Which one is NOT one of the recycling product?",
            answers = listOf("Chemicals", "paper", "plastic", "glass")
        ),
        Question(
            text = "What is the term used to describe object that can washed and used again?",
            answers = listOf(
                "Reusing",
                "Recycling",
                "Reducing",
                "Buying"
            )
        ),
        Question(
            text = "Which of the following can be recycled?",
            answers = listOf("NavController", "NavCentral", "NavMaster", "NavSwitcher")
        ),
        Question(
            text = "Which XML element lets you register an activity with the launcher activity?",
            answers = listOf("Soda bottle", "Paper plates", "plastic spoon", "Zip lock bags")
        ),
        Question(
            text = "WRe-processin material to make another product?",
            answers = listOf("Recycle", "Reduce", "Reuse", "Buying")
        )
    )

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questions.size + 1) / 2, 5)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_quiz, container, false)
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentQuiz1Binding>(
            inflater, R.layout.fragment_quiz1, container, false
        )

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        // We've won!  Navigate to the gameWonFragment.
                        view.findNavController()
                            .navigate(R.id.home)
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController()
                        .navigate(R.id.home)
                }
            }

        }
        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_Recycle_question, questionIndex + 1, numQuestions)
    }
}
