object QuizTracker {
    var solvedQuizzes: Int = 0
    var wrongAnswers: Int = 0

    fun incrementSolvedQuizzes() {
        solvedQuizzes++
    }

    fun incrementWrongAnswers() {
        wrongAnswers++
    }

    fun resetCounters() {
        solvedQuizzes = 0
        wrongAnswers = 0
    }
}
