object QuizTracker {
    var solvedQuizzes: Int = 0
    var Solved: Int = 0
    var wrongAnswers: Int = 0
    var level: Int = 1
    var xp: Int = 0
    var gainedXp: Int = 0
    val xpPerLevel: Int = 60
    var DailyGoalsWrong: Int = 0
    var SolvedTopics: Int = 0
    var FullXP: Int = 0
    var XPDisplay = 30

    fun incrementSolvedQuizzes() {
        solvedQuizzes++
        checkLevelUp()
    }

    fun incrementWrongAnswers() {
        wrongAnswers++
    }

    fun incrementSolved() {
        Solved++
    }

    private fun checkLevelUp() {
        while (xp >= xpPerLevel) {
            xp -= xpPerLevel
            level++
            gainedXp = 0
        }
    }

    fun resetCounters() {
        solvedQuizzes = 0
        wrongAnswers = 0
        // XP remains unchanged
    }

    fun addXp(amount: Int) {
        xp += amount
        gainedXp += amount
        checkLevelUp()
    }

    fun incrementDailyWrongAnswers() {
        DailyGoalsWrong++
    }

    fun incrementSolvedTopics(){
        SolvedTopics++
    }
}


