package com.example.yfl

// Quiz data class to represent the entire quiz
data class Quiz(
    val quiz: List<Question>  // This should match the JSON root property
)

// Question data class to represent a question and its answers
data class Question(
    val question: String,   // The question text
    val answers: List<Answer>  // A list of possible answers
)

// Answer data class to represent each answer option
data class Answer(
    val text: String,       // The text of the answer
    val isCorrect: Boolean  // Whether the answer is correct
)

