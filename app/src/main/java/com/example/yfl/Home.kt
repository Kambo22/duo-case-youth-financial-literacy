package com.example.yfl

import QuizTracker.DailyGoalsWrong
import QuizTracker.Solved
import QuizTracker.SolvedTopics
import QuizTracker.gainedXp
import QuizTracker.level
import QuizTracker.xp
import QuizTracker.xpPerLevel
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

    private lateinit var solve: TextView
    private lateinit var solvequizzes: TextView
    private lateinit var mistakes: TextView
    private lateinit var levelText: TextView
    private lateinit var xpText: TextView
    private lateinit var quizzesDoneText: TextView
    private lateinit var solveQuizButton: Button
    private lateinit var goal1Card: LinearLayout
    private lateinit var goal2Card: LinearLayout
    private lateinit var goal3Card: LinearLayout

    private var quizzesDoneToday = 0

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
        solve = findViewById(R.id.solve)
        mistakes = findViewById(R.id.mistakes)
        solvequizzes = findViewById(R.id.solvequizzes)
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
        xp = sharedPreferences.getInt("xp", gainedXp)
        level = sharedPreferences.getInt("level", level)
        quizzesDoneToday = sharedPreferences.getInt("quizzesDoneToday", 0)
    }

    private fun updateUI() {
        levelText.text = "$level Level"
        xpText.text = "$gainedXp/$xpPerLevel Xp"
        quizzesDoneText.text = "$Solved"
        solve.text = "$SolvedTopics/1"
        solvequizzes.text = "$Solved/3"
        mistakes.text = "$DailyGoalsWrong"

        updateGoalCardBackgrounds()
    }

    private fun updateGoalCardBackgrounds() {

        when {
            Solved == 0 -> goal1Card.setBackgroundResource(R.drawable.rounded_card_orange)
            Solved in 1..2 -> goal1Card.setBackgroundResource(R.drawable.rounded_card_blue)
            Solved >= 3 -> goal1Card.setBackgroundResource(R.drawable.rounded_edges_lgreen)
        }


        when {
            DailyGoalsWrong <= 5 -> goal2Card.setBackgroundResource(R.drawable.rounded_edges_lgreen)
            DailyGoalsWrong > 5 -> goal2Card.setBackgroundResource(R.drawable.rounded_card_orange)
        }


        when {
            SolvedTopics == 0 -> goal3Card.setBackgroundResource(R.drawable.rounded_card_orange)
            SolvedTopics >= 1 -> goal3Card.setBackgroundResource(R.drawable.rounded_edges_lgreen)
        }
    }

    private fun saveUserData() {
        with(sharedPreferences.edit()) {
            putInt("userXP", xp)
            putInt("userLevel", level)
            putInt("quizzesDoneToday", quizzesDoneToday)
            apply()
        }
    }

    private fun openQuizActivity() {
        val intent = Intent(this, QuizzTopics::class.java)
        startActivity(intent)
        saveUserData()
        updateUI()
    }

    private fun checkDailyReset() {
        val lastReset = sharedPreferences.getLong("lastReset", 0L)
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        if (lastReset != currentDate.toLong()) {
            quizzesDoneToday = 0
            sharedPreferences.edit().putInt("quizzesDoneToday", quizzesDoneToday).apply()
            sharedPreferences.edit().putLong("lastReset", currentDate.toLong()).apply()
            updateUI()
        }
    }
}
