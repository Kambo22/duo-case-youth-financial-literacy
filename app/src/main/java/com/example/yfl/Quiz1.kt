package com.example.yfl

import QuizTracker
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

class Quiz1 : AppCompatActivity() {

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
    private var incorrectQuestions = mutableListOf<Int>()

    private val correctGifs = listOf(R.drawable.correct1, R.drawable.correct2, R.drawable.correct3)
    private val wrongGifs = listOf(R.drawable.wrong1, R.drawable.wrong2, R.drawable.wrong3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz1)


        val floatAnimation = AnimationUtils.loadAnimation(this, R.anim.float_animation)

        findViewById<View>(R.id.Question1).startAnimation(floatAnimation)

        findViewById<View>(R.id.QuestionDesc).startAnimation(floatAnimation)
        findViewById<View>(R.id.QuestionAns).startAnimation(floatAnimation)

        correctSound = MediaPlayer.create(this, R.raw.kaching)
        wrongSound = MediaPlayer.create(this, R.raw.wrong)

        questionDesc = findViewById(R.id.QuestionDesc)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        questionBox = findViewById(R.id.Question1)
        button4new = findViewById(R.id.button4New)
        resultGif = findViewById(R.id.questionGif)

        loadQuizData()

        setQuestion()

        val returnButton = findViewById<ImageButton>(R.id.returnBTN)
        returnButton.setOnClickListener {
            // Create an AlertDialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm Exit")
                .setMessage("Are you sure you want to leave the quiz?")
                .setPositiveButton("Yes") { dialog, _ ->
                    // Reset counters
                    QuizTracker.resetCounters()

                    // Navigate to the home page
                    val intent = Intent(this, SavingTIps::class.java)
                    startActivity(intent)
                    finish()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }

            builder.create().show()
        }

        // Button listeners
        button1.setOnClickListener { handleAnswerClick(0) }
        button2.setOnClickListener { handleAnswerClick(1) }
        button3.setOnClickListener { handleAnswerClick(2) }

        button4new.setOnClickListener { loadNextQuestion() }
    }

    private fun loadQuizData() {
        val inputStream = resources.openRawResource(R.raw.quiz_data)
        val reader = InputStreamReader(inputStream)
        val gson = Gson()
        val quiz = gson.fromJson(reader, Quiz::class.java)
        quizList = quiz.quiz.shuffled()
    }

    private fun setQuestion() {
        val question = quizList[currentQuestionIndex]
        questionDesc.text = question.question

        button1.text = question.answers[0].text
        button2.text = question.answers[1].text
        button3.text = question.answers[2].text

        val answerDescription = question.answerDescription.replace(", ", "\n")
        findViewById<TextView>(R.id.QuestionAns).text = answerDescription

        resetButtonStates()
    }

    private fun updateLoadingBar() {
        // Convert dp to pixels
        val density = resources.displayMetrics.density
        val loadingBar = findViewById<View>(R.id.loadingbar)

        val loadingBarWidth = 64 + (QuizTracker.solvedQuizzes * (256 / 6))
        val cappedWidth = loadingBarWidth.coerceIn(64, 290)

        val newWidthInPixels = (cappedWidth * density).toInt()
        val layoutParams = loadingBar.layoutParams
        layoutParams.width = newWidthInPixels
        loadingBar.layoutParams = layoutParams
    }

    private fun handleAnswerClick(selectedIndex: Int) {
        val correctIndex = quizList[currentQuestionIndex].answers.indexOfFirst { it.isCorrect }

        updateButtonBackgrounds(correctIndex, selectedIndex)

        if (selectedIndex == correctIndex) {
            questionBox.setBackgroundResource(R.drawable.questionright)
            correctSound.start()
            loadGif(true)
            QuizTracker.incrementSolvedQuizzes()
            incorrectQuestions.remove(currentQuestionIndex)

            updateLoadingBar()

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

            button4new.text = "Try Again"
        }

        lockButtons()

        Handler().postDelayed({
            resultGif.visibility = View.GONE

            Handler().postDelayed({
                button4new.visibility = View.VISIBLE
            }, 1000)
        }, 2000)
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
            if (currentQuestionIndex >= quizList.size) {
                currentQuestionIndex = 0
            }
        }

        if (QuizTracker.solvedQuizzes >= 6) {
            QuizTracker.addXp(30)
            QuizTracker.incrementSolvedTopics()
            QuizTracker.incrementSolved()
            val intent = Intent(this, results::class.java)
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