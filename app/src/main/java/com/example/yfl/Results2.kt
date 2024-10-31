package com.example.yfl

import QuizTracker
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class Results2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_results2)

        val floatAnimation = AnimationUtils.loadAnimation(this, R.anim.float_animation)

        findViewById<View>(R.id.Result1).startAnimation(floatAnimation)
        findViewById<View>(R.id.Result2).startAnimation(floatAnimation)
        findViewById<View>(R.id.Result3).startAnimation(floatAnimation)

        // Display wrong answers in result1
        val wrongAnswersTextView = findViewById<TextView>(R.id.result1)
        wrongAnswersTextView.text = "${QuizTracker.wrongAnswers}"

        val returnButton = findViewById<ImageButton>(R.id.returnBTN)
        returnButton.setOnClickListener {
            // Create an AlertDialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm Exit")
                .setMessage("Are you sure you want to leave the quiz?")
                .setPositiveButton("Yes") { dialog, _ ->
                    // Reset counters
                    QuizTracker.resetCounters() // Add this method in QuizTracker class

                    // Navigate to the home page
                    val intent = Intent(this, BudgetTopic::class.java) // Replace HomeActivity with your actual home page class
                    startActivity(intent)
                    finish() // Optionally finish the current activity
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss() // Just close the dialog
                }

            // Show the dialog
            builder.create().show()
        }

        val restartQuizButton = findViewById<Button>(R.id.Goback)

        restartQuizButton.setOnClickListener {
            QuizTracker.resetCounters() // Reset the counters

            // Create an Intent to start the Quiz1 activity
            val intent = Intent(this, Quiz2::class.java)
            startActivity(intent) // Start Quiz1 activity
            finish() // Close the results activity
        }

        // Change background of Result1 view based on the number of wrong answers
        val result1View = findViewById<View>(R.id.Result1) // Reference to the Result1 view
        when (QuizTracker.wrongAnswers) {
            in 0..3 -> result1View.setBackgroundResource(R.drawable.questionright) // 0, 1, 2, 3 wrong answers
            in 4..7 -> result1View.setBackgroundResource(R.drawable.question) // 4, 5, 6, 7 wrong answers
            else -> result1View.setBackgroundResource(R.drawable.questionwrong) // 8 or more wrong answers
        }

        // Calculate and display grade in result3
        val grade = when (QuizTracker.wrongAnswers) {
            0, 1 -> "A"
            2, 3 -> "B"
            4, 5 -> "C"
            6, 7 -> "D"
            8, 9 -> "E"
            else -> "F"
        }

        val gradeTextView = findViewById<TextView>(R.id.result3)
        gradeTextView.text = grade

        // Change background of Result3 view based on grade
        val gradeView = findViewById<View>(R.id.Result3) // Reference to the Result3 view
        when (grade) {
            "A", "B" -> gradeView.setBackgroundResource(R.drawable.questionright)
            "C", "D", "E" -> gradeView.setBackgroundResource(R.drawable.question)
            "F" -> gradeView.setBackgroundResource(R.drawable.questionwrong)
        }

        // Display level in level TextView
        val levelTextView = findViewById<TextView>(R.id.level)
        levelTextView.text = "Level: ${QuizTracker.level}"

        // Display gained XP in result2
        val xpTextView = findViewById<TextView>(R.id.result2)
        xpTextView.text = "+ ${QuizTracker.XPDisplay}" // Change to gained XP
    }
}
