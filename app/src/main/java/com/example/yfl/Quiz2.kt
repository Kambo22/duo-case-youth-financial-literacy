package com.example.yfl

import QuizTracker.incrementSolvedTopics
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.InputStreamReader
import android.media.MediaPlayer
import android.widget.ImageView
import com.bumptech.glide.Glide
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import kotlin.random.Random
import androidx.appcompat.app.AlertDialog

class Quiz2 : AppCompatActivity() {

    private lateinit var correctSound: MediaPlayer
    private lateinit var wrongSound: MediaPlayer
    private lateinit var questionDesc: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var questionBox: View
    private lateinit var button4new: Button
    private lateinit var resultGif: ImageView
    private var loadingBarWidth = 64

    private var currentQuestionIndex = 0
    private lateinit var quizList: List<Question>
    private var incorrectQuestions = mutableListOf<Int>() // List to track incorrect questions

    private val correctGifs = listOf(R.drawable.correct1, R.drawable.correct2, R.drawable.correct3)
    private val wrongGifs = listOf(R.drawable.wrong1, R.drawable.wrong2, R.drawable.wrong3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz2)

        correctSound = MediaPlayer.create(this, R.raw.kaching)
        wrongSound = MediaPlayer.create(this, R.raw.wrong)


        val floatAnimation = AnimationUtils.loadAnimation(this, R.anim.float_animation)

        findViewById<View>(R.id.Question1).startAnimation(floatAnimation)
        findViewById<View>(R.id.QuestionDesc).startAnimation(floatAnimation)
        findViewById<View>(R.id.QuestionAns).startAnimation(floatAnimation)


        questionDesc = findViewById(R.id.QuestionDesc)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        questionBox = findViewById(R.id.Question1)
        button4new = findViewById(R.id.button4New)
        resultGif = findViewById(R.id.questionGif) // Initialize the new button

        // Load JSON data
        loadQuizData()

        // Set the first question
        setQuestion()

        // Set up return button listener
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

        // Button listeners
        button1.setOnClickListener { handleAnswerClick(0) }
        button2.setOnClickListener { handleAnswerClick(1) }
        button3.setOnClickListener { handleAnswerClick(2) }

        // Set the listener for button4new to load the next question
        button4new.setOnClickListener { loadNextQuestion() }
    }

    private fun loadQuizData() {
        val inputStream = resources.openRawResource(R.raw.quiz_data2) // Ensure your JSON file is in res/raw
        val reader = InputStreamReader(inputStream)
        val gson = Gson()
        val quiz = gson.fromJson(reader, Quiz::class.java) // Deserialize to Quiz class
        quizList = quiz.quiz.shuffled() // Shuffle the list of questions
    }

    private fun setQuestion() {
        val question = quizList[currentQuestionIndex]
        questionDesc.text = question.question

        // Set button texts based on JSON data
        button1.text = question.answers[0].text
        button2.text = question.answers[1].text
        button3.text = question.answers[2].text

        val answerDescription = question.answerDescription.replace(", ", "\n") // Replace commas with new lines
        findViewById<TextView>(R.id.QuestionAns).text = answerDescription

        // Reset button states and hide button4new
        resetButtonStates()
    }

    private fun updateLoadingBar() {
        // Convert dp to pixels
        val density = resources.displayMetrics.density
        val loadingBar = findViewById<View>(R.id.loadingbar)

        // Calculate the new width based on the number of correct answers
        val loadingBarWidth = 64 + (QuizTracker.solvedQuizzes * (256 / 6)) // Increment width proportionally
        val cappedWidth = loadingBarWidth.coerceIn(64, 290) // Ensure it doesn't go below 64dp or above 320dp

        // Set the new width
        val newWidthInPixels = (cappedWidth * density).toInt()
        val layoutParams = loadingBar.layoutParams
        layoutParams.width = newWidthInPixels
        loadingBar.layoutParams = layoutParams
    }

    private fun handleAnswerClick(selectedIndex: Int) {
        val correctIndex = quizList[currentQuestionIndex].answers.indexOfFirst { it.isCorrect }

        // Update button backgrounds
        updateButtonBackgrounds(correctIndex, selectedIndex)

        // Handle the answer outcome
        if (selectedIndex == correctIndex) {
            questionBox.setBackgroundResource(R.drawable.questionright)
            correctSound.start()
            loadGif(true)
            QuizTracker.incrementSolvedQuizzes() // Increment solved quizzes
            incorrectQuestions.remove(currentQuestionIndex) // Remove from incorrect if answered correctly

            // Update loading bar width
            updateLoadingBar()

            // Change button text to "Proceed to the next question" or "See the Results"
            if (QuizTracker.solvedQuizzes >= 6) {
                button4new.text = "See the Results"
            } else {
                button4new.text = "Proceed to the next question"
            }
        } else {
            questionBox.setBackgroundResource(R.drawable.questionwrong)
            wrongSound.start()
            loadGif(false)
            QuizTracker.incrementWrongAnswers()
            QuizTracker.incrementDailyWrongAnswers()
            if (!incorrectQuestions.contains(currentQuestionIndex)) {
                incorrectQuestions.add(currentQuestionIndex)
            }

            // Change button text to "Try Again"
            button4new.text = "Try Again"
        }

        // Lock buttons
        lockButtons()

        // Hide the GIF and show the next question button after a delay
        Handler().postDelayed({
            resultGif.visibility = View.GONE

            // Show button4new after the GIF is hidden
            Handler().postDelayed({
                button4new.visibility = View.VISIBLE
            }, 1000) // 1 second delay for button4new
        }, 2000) // 2 seconds delay for hiding the GIF
    }




    private fun updateButtonBackgrounds(correctIndex: Int, selectedIndex: Int) {
        val buttons = listOf(button1, button2, button3)
        buttons.forEachIndexed { index, button ->
            button.setBackgroundResource(if (index == correctIndex) R.drawable.rightans else R.drawable.wrongans)
        }
    }


    private fun loadGif(isCorrect: Boolean) {
        val gifResId = when (isCorrect) {
            true -> correctGifs[Random.nextInt(correctGifs.size)]
            false -> wrongGifs[Random.nextInt(wrongGifs.size)]
        }

        resultGif.visibility = View.VISIBLE
        Glide.with(this).load(gifResId).into(resultGif)
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
        if (incorrectQuestions.isNotEmpty()) {
            val randomIndex = Random.nextInt(incorrectQuestions.size)
            currentQuestionIndex = incorrectQuestions[randomIndex]
            incorrectQuestions.removeAt(randomIndex)
        } else {
            currentQuestionIndex++
            // Check if the quiz is completed
            if (currentQuestionIndex >= quizList.size) {
                currentQuestionIndex = 0 // Reset if all questions are answered
            }
        }

        // Check if the user has reached 6 solved quizzes
        if (QuizTracker.solvedQuizzes >= 6) {
            QuizTracker.addXp(30) // Add 30 XP here
            incrementSolvedTopics()
            val intent = Intent(this, Results2::class.java)
            startActivity(intent)
            finish()
        } else {
            setQuestion()
            resultGif.visibility = View.GONE
        }
    }



    override fun onDestroy() {
        correctSound.release()
        wrongSound.release()
        super.onDestroy()
    }
}