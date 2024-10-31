package com.example.yfl

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AnimationUtils


class QuizzTopics : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quizz_topics)

        // Load the animation
        val floatAnimation = AnimationUtils.loadAnimation(this, R.anim.float_animation)

        // Find the views and start the animation
        findViewById<View>(R.id.Topic1).startAnimation(floatAnimation)
        findViewById<View>(R.id.Topic2).startAnimation(floatAnimation)

        findViewById<View>(R.id.insideText1).startAnimation(floatAnimation)
        findViewById<View>(R.id.insideText2).startAnimation(floatAnimation)

        // Find the views and start the animation
        val topic1 = findViewById<View>(R.id.Topic1)
        val topic2 = findViewById<View>(R.id.Topic2)

        val returnButton = findViewById<View>(R.id.returnBTN)

        returnButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        topic1.setOnClickListener {
            val intent = Intent(this, SavingTIps::class.java)
            startActivity(intent)
        }

        topic2.setOnClickListener {
            val intent = Intent(this, BudgetTopic::class.java)
            startActivity(intent)
        }
    }
}