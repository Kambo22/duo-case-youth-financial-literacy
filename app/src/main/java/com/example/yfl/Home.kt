package com.example.yfl

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class Home : AppCompatActivity() {

    private lateinit var levelText: TextView
    private lateinit var xpText: TextView
    private lateinit var quizzesDoneText: TextView
    private lateinit var solveQuizButton: Button
    private lateinit var goal1Card: LinearLayout
    private lateinit var goal2Card: LinearLayout
    private lateinit var goal3Card: LinearLayout

    private var userXP = 0
    private var quizzesDoneToday = 0
    private val xpPerQuiz = 10
    private val dailyQuizLimit = 5

    private val sharedPreferences by lazy {
        getSharedPreferences("UserProgress", MODE_PRIVATE)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val floatAnimation = AnimationUtils.loadAnimation(this, R.anim.float_animation)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        findViewById<View>(R.id.DailyGoals).startAnimation(floatAnimation)
        findViewById<View>(R.id.levelScreen).startAnimation(floatAnimation)
        findViewById<View>(R.id.Dailyquiz).startAnimation(floatAnimation)

        levelText = findViewById(R.id.levelText)
        xpText = findViewById(R.id.xpText)
        quizzesDoneText = findViewById(R.id.quizzesDone)
        solveQuizButton = findViewById(R.id.solveQuizButton)
        goal1Card = findViewById(R.id.goal1Card)
        goal2Card = findViewById(R.id.goal2Card)
        goal3Card = findViewById(R.id.goal3Card)

        loadUserData()
        updateUI()

        solveQuizButton.setOnClickListener { openQuizActivity() }

        checkDailyReset()
    }

    private fun loadUserData() {
        userXP = sharedPreferences.getInt("userXP", 0)
        levelText.text = "Level: ${QuizTracker.level}"
        quizzesDoneToday = sharedPreferences.getInt("quizzesDoneToday", 0)
    }

    private fun updateUI() {
        levelText.text = "Level: ${QuizTracker.level}"
        xpText.text = "$userXP/30 XP"
        quizzesDoneText.text = "Quizzes done today: $quizzesDoneToday"

        // Update goal card backgrounds based on task status
        setGoalCardBackground(goal1Card, quizzesDoneToday >= 1)
        setGoalCardBackground(goal2Card, quizzesDoneToday >= 3)
        setGoalCardBackground(goal3Card, quizzesDoneToday >= 5)
    }

    private fun setGoalCardBackground(card: LinearLayout, isComplete: Boolean) {
        when {
            isComplete -> card.background = ContextCompat.getDrawable(this, R.drawable.rounded_card_green)
            quizzesDoneToday > 0 -> card.background = ContextCompat.getDrawable(this, R.drawable.rounded_card_orange)
            else -> card.background = ContextCompat.getDrawable(this, R.drawable.rounded_card_red)
        }
    }

    private fun addXP(xp: Int) {
        userXP += xp
        if (userXP >= 30) {
            userXP -= 30

        }
        saveUserData()
        updateUI()
    }

    private fun saveUserData() {
        with(sharedPreferences.edit()) {
            putInt("userXP", userXP)
            putInt("quizzesDoneToday", quizzesDoneToday)
            apply()
        }
    }

    private fun openQuizActivity() {
        val intent = Intent(this, QuizzTopics::class.java)
        startActivity(intent)
        // Simulate completion of a quiz and add XP
        quizzesDoneToday++
        saveUserData()
        updateUI()
    }

    private fun checkDailyReset() {
        val lastReset = sharedPreferences.getLong("lastReset", 0L)
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        // If the date has changed since the last reset, reset the daily progress
        if (lastReset != currentDate.toLong()) {
            quizzesDoneToday = 0
            sharedPreferences.edit().putInt("quizzesDoneToday", quizzesDoneToday).apply()
            sharedPreferences.edit().putLong("lastReset", currentDate.toLong()).apply()
            updateUI()
        }
    }
}
