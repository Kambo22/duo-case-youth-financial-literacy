package com.example.yfl

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.InputStreamReader
import android.media.MediaPlayer


class Quiz1 : AppCompatActivity() {

    private lateinit var correctSound: MediaPlayer
    private lateinit var wrongSound: MediaPlayer
    private lateinit var questionDesc: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var questionBox: View
    private lateinit var button4new: Button // Added to reference the new button

    private var currentQuestionIndex = 0
    private lateinit var quizList: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz1)


        correctSound = MediaPlayer.create(this, R.raw.kaching)
        wrongSound = MediaPlayer.create(this, R.raw.wrong)

        questionDesc = findViewById(R.id.QuestionDesc)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        questionBox = findViewById(R.id.Question1)
        button4new = findViewById(R.id.button4New) // Initialize the new button

        // Load JSON data
        loadQuizData()

        // Set the first question
        setQuestion()

        // Button listeners
        button1.setOnClickListener { handleAnswerClick(0) }
        button2.setOnClickListener { handleAnswerClick(1) }
        button3.setOnClickListener { handleAnswerClick(2) }

        // Set the listener for button4new to load the next question
        button4new.setOnClickListener { loadNextQuestion() }
    }

    private fun loadQuizData() {
        val inputStream = resources.openRawResource(R.raw.quiz_data) // Ensure your JSON file is in res/raw
        val reader = InputStreamReader(inputStream)
        val gson = Gson()
        val quiz = gson.fromJson(reader, Quiz::class.java) // Deserialize to Quiz class
        quizList = quiz.quiz // Assign the list of questions to quizList
    }

    private fun setQuestion() {
        val question = quizList[currentQuestionIndex]
        questionDesc.text = question.question

        // Set button texts based on JSON data
        button1.text = question.answers[0].text
        button2.text = question.answers[1].text
        button3.text = question.answers[2].text

        // Reset button states and hide button4new
        resetButtonStates()
    }

    private fun handleAnswerClick(selectedIndex: Int) {
        val correctIndex = quizList[currentQuestionIndex].answers.indexOfFirst { it.isCorrect }

        // Change backgrounds for all buttons
        val buttons = listOf(button1, button2, button3)
        buttons.forEachIndexed { index, button ->
            if (index == correctIndex) {
                button.setBackgroundResource(R.drawable.rightans)
            } else {
                button.setBackgroundResource(R.drawable.wrongans)
            }
        }

        // Change the background of the questionBox based on the selected answer
        if (selectedIndex == correctIndex) {
            questionBox.setBackgroundResource(R.drawable.questionright)
            correctSound.start()// Correct answer background
        } else {
            questionBox.setBackgroundResource(R.drawable.questionwrong)
            wrongSound.start()
        }

        // Lock buttons after an answer is chosen
        lockButtons()

        // Show the next question button
        button4new.visibility = View.VISIBLE // Make button4new visible after answering
    }

    private fun lockButtons() {
        button1.isEnabled = false
        button2.isEnabled = false
        button3.isEnabled = false
    }

    private fun resetButtonStates() {
        val buttons = listOf(button1, button2, button3)
        buttons.forEach { button ->
            button.isEnabled = true
            button.setBackgroundResource(R.drawable.answer_button)
        }
        button4new.visibility = View.GONE
        questionBox.setBackgroundResource(R.drawable.question)
    }

    private fun loadNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < quizList.size) {
            setQuestion()
        } else {
            // Handle quiz completion (optional)
        }
    }

    override fun onDestroy() {
        correctSound.release()
        wrongSound.release()
        super.onDestroy()
    }
}

