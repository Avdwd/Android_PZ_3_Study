package com.example.pz_3_shynkarenko

class GameEngine {

    var totalAnswers = 0
        private set

    var score = 0
        private set

    private var isCurrentAnswerCorrect = false
    fun nextRound(): GameRound {
        val nameOnLeft = Colors.getRandomColor()
        val textOnRight = Colors.getRandomColor()
        val isCorrectRound = (0..1).random() == 1
        val colorOnRight: Colors

        if (isCorrectRound) {
            colorOnRight = nameOnLeft
            isCurrentAnswerCorrect = true
        } else {
            var tempColor = Colors.getRandomColor()
            while (tempColor == nameOnLeft) {
                tempColor = Colors.getRandomColor()
            }
            colorOnRight = tempColor
            isCurrentAnswerCorrect = false
        }

        return GameRound(
            leftText = nameOnLeft.colorName,
            rightText = textOnRight.colorName,
            rightTextColorValue = colorOnRight.rgb
        )
    }

    fun checkAnswer(userAnswer: Boolean): Boolean {
        totalAnswers++
        val wasCorrect = (userAnswer == isCurrentAnswerCorrect)
        if (wasCorrect) {
            score++
        }
        return wasCorrect
    }

    fun resetGame() {
        score = 0
        totalAnswers = 0
    }

    fun restoreState(savedScore: Int, savedTotalAnswers: Int) {
        score = savedScore
        totalAnswers = savedTotalAnswers
    }
}

data class GameRound(
    val leftText: String,
    val rightText: String,
    val rightTextColorValue: Int
)
