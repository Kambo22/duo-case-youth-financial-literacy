package com.example.yfl


data class Quiz(
    val quiz: List<Question>
)


data class Question(
    val question: String,
    val answers: List<Answer>,
    val answerDescription: String
)


data class Answer(
    val text: String,
    val isCorrect: Boolean
)

