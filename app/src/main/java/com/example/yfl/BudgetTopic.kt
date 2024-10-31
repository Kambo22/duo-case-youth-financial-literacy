package com.example.yfl

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.animation.AnimationUtils

class BudgetTopic : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budget_topic)

        val floatAnimation = AnimationUtils.loadAnimation(this, R.anim.float_animation)

        findViewById<View>(R.id.Topic1).startAnimation(floatAnimation)

        findViewById<View>(R.id.insideText1).startAnimation(floatAnimation)

        val btn = findViewById<View>(R.id.topicButton)

        val returnButton = findViewById<View>(R.id.returnBTN)

        returnButton.setOnClickListener {
            val intent = Intent(this, QuizzTopics::class.java)
            startActivity(intent)
        }

        btn.setOnClickListener {
            val intent = Intent(this, Quiz2::class.java)
            startActivity(intent)
        }


    }
}