object QuizTracker {
    var solvedQuizzes: Int = 0
    var wrongAnswers: Int = 0
    var level: Int = 1
    var xp: Int = 0
    var gainedXp: Int = 0 // New property to track gained XP
    private val xpPerLevel: Int = 30
    var DailyGoalsWrong: Int = 0
    var SolvedTopics: Int = 0

    fun incrementSolvedQuizzes() {
        solvedQuizzes++
        checkLevelUp()
    }

    fun incrementWrongAnswers() {
        wrongAnswers++
    }

    private fun checkLevelUp() {
        while (xp >= xpPerLevel) {
            xp -= xpPerLevel
            level++
        }
    }

    fun resetCounters() {
        solvedQuizzes = 0
        wrongAnswers = 0
        // XP remains unchanged
        gainedXp = 0 // Reset gained XP
    }

    fun addXp(amount: Int) {
        xp += amount
        gainedXp += amount // Add to gained XP
        checkLevelUp()
    }

    fun incrementDailyWrongAnswers() {
        DailyGoalsWrong++
    }

    fun incrementSolvedTopics(){
        SolvedTopics++
    }
}


