package com.example.yfl

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class test : AppCompatActivity() {
    private lateinit var rightAnswersCount: TextView
    private lateinit var wrongAnswersCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test)

        // Initialize TextViews
        rightAnswersCount = findViewById(R.id.rightAnswersCount)
        wrongAnswersCount = findViewById(R.id.wrongAnswersCount)

        // Display the counts from QuizTracker
        updateCounts()
    }

    private fun updateCounts() {
        val rightAnswers = QuizTracker.solvedQuizzes
        val wrongAnswers = QuizTracker.wrongAnswers

        rightAnswersCount.text = "Correct Answers: $rightAnswers"
        wrongAnswersCount.text = "Wrong Answers: $wrongAnswers"
    }
}
