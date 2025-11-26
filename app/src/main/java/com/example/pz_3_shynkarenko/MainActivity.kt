package com.example.pz_3_shynkarenko

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View // *** NEW: Потрібно для View.GONE / View.VISIBLE ***
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pz_3_shynkarenko.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val gameEngine = GameEngine()


    private var gameTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonYes.setOnClickListener {
            handleAnswer(true)
        }

        binding.buttonNo.setOnClickListener {
            handleAnswer(false)
        }

        binding.buttonRestart.setOnClickListener {
            restartGame()
        }


        binding.buttonRestart.visibility = View.GONE


        startGameTimer()
        updateScoreDisplay()
        loadNextRound()
    }

    private fun loadNextRound() {

        val roundData = gameEngine.nextRound()
        binding.tvColorName.text = roundData.leftText
        binding.tvColor.text = roundData.rightText
        binding.tvColor.setTextColor(roundData.rightTextColorValue)
    }

    private fun handleAnswer(userAnswer: Boolean) {

        val wasCorrect = gameEngine.checkAnswer(userAnswer)
        if (wasCorrect) {
            Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Помилка!", Toast.LENGTH_SHORT).show()
        }
        updateScoreDisplay()
        loadNextRound()
    }

    private fun updateScoreDisplay() {
        binding.progressBar.max = gameEngine.totalAnswers
        binding.progressBar.progress = gameEngine.score
        binding.score.text = "Рахунок: ${gameEngine.score}"
    }


    private fun startGameTimer() {

        gameTimer?.cancel()

        gameTimer = object : CountDownTimer(60000, 1000) { // 60 секунд
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.text = "Час: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                binding.timer.text = "Час вийшов!"

                Toast.makeText(this@MainActivity, "Гра завершена! Ваш рахунок: ${gameEngine.score}", Toast.LENGTH_LONG).show()
                binding.tvQuestion.text = "Гра завершена! Ваш рахунок: ${gameEngine.score}"

                binding.buttonYes.isEnabled = false
                binding.buttonNo.isEnabled = false

                binding.buttonRestart.visibility = View.VISIBLE
            }
        }.start()
    }


    private fun restartGame() {

        binding.buttonRestart.visibility = View.GONE


        binding.buttonYes.isEnabled = true
        binding.buttonNo.isEnabled = true
        gameEngine.resetGame()

        updateScoreDisplay()
        binding.tvQuestion.text = "Чи співпадає кольор тексту з назвою кольору?"
        loadNextRound()
        startGameTimer()
    }
}

