package com.example.yfl

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.animation.AnimationUtils
import android.widget.Button

class Quiz2 : AppCompatActivity() {

    private lateinit var loadingBar: View
    private lateinit var button1R: Button
    private lateinit var button2W: Button
    private lateinit var button3W: Button
    private lateinit var button4new: Button
    private lateinit var questionView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz2)

        val floatAnimation = AnimationUtils.loadAnimation(this, R.anim.float_animation)

        findViewById<View>(R.id.Question1).startAnimation(floatAnimation)
        findViewById<View>(R.id.QuestionDesc).startAnimation(floatAnimation)
        findViewById<View>(R.id.QuestionAns).startAnimation(floatAnimation)
        findViewById<View>(R.id.button4New).startAnimation(floatAnimation)

        val returnButton = findViewById<View>(R.id.returnBTN)

        val nextButton = findViewById<View>(R.id.button4New)

        nextButton.setOnClickListener {
            val intent = Intent(this, test::class.java)
            startActivity(intent)
        }

        returnButton.setOnClickListener {
            val intent = Intent(this, SavingTIps::class.java)
            startActivity(intent)
        }

        // Initialize views
        button1R = findViewById(R.id.button1R)
        button2W = findViewById(R.id.button2W)
        button3W = findViewById(R.id.button3W)
        button4new = findViewById(R.id.button4New)
        loadingBar = findViewById(R.id.loadingbar)
        questionView = findViewById(R.id.Question1)

        button4new.visibility = View.GONE

        // Set onClickListeners for buttons
        button1R.setOnClickListener {
            handleAnswerSelection(false)
        }

        button2W.setOnClickListener {
            handleAnswerSelection(true)
        }

        button3W.setOnClickListener {
            handleAnswerSelection(false)
        }
    }

    private fun handleAnswerSelection(isCorrect: Boolean) {
        // Change the background of all buttons
        button1R.setBackgroundResource(R.drawable.wrongans) // Correct answer button background
        button2W.setBackgroundResource(R.drawable.rightans) // Wrong answer button background
        button3W.setBackgroundResource(R.drawable.wrongans) // Wrong answer button background

        // Change the background of the question view based on the selection
        if (isCorrect) {
            questionView.setBackgroundResource(R.drawable.questionright)
            QuizTracker.incrementSolvedQuizzes()
        } else {
            questionView.setBackgroundResource(R.drawable.questionwrong)
            QuizTracker.incrementWrongAnswers()
        }

        // Increase loading bar width by +64dp
        val params = loadingBar.layoutParams
        params.width += (64 * resources.displayMetrics.density).toInt() // Convert 64dp to pixels
        loadingBar.layoutParams = params

        // Lock all buttons to prevent further changes
        button1R.isEnabled = false
        button2W.isEnabled = false
        button3W.isEnabled = false

        button4new.postDelayed({
            button4new.visibility = View.VISIBLE
        }, 2000)

    }

}
