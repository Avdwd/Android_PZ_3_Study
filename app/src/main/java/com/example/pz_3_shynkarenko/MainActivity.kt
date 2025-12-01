package com.example.pz_3_shynkarenko

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pz_3_shynkarenko.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val gameEngine = GameEngine()
    private var gameTimer: CountDownTimer? = null
    private var currentUserEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserEmail = intent.getStringExtra("CURRENT_USER_EMAIL")


        binding.buttonYes.setOnClickListener { handleAnswer(true) }
        binding.buttonNo.setOnClickListener { handleAnswer(false) }
        binding.buttonRestart.setOnClickListener { restartGame() }


        binding.buttonStart.setOnClickListener {
            startGame()
        }


        setupInitialScreen()
    }


    private fun setupInitialScreen() {
        binding.buttonStart.visibility = View.VISIBLE

        binding.buttonYes.visibility = View.GONE
        binding.buttonNo.visibility = View.GONE
        binding.buttonRestart.visibility = View.GONE

        binding.tvQuestion.text = "Натисніть Старт, щоб почати"
        binding.timer.text = "Час: 60"
        binding.score.text = "Рахунок: 0"


        binding.tvColorName.text = ""
        binding.tvColor.text = ""
    }


    private fun startGame() {

        binding.buttonStart.visibility = View.GONE


        binding.buttonYes.visibility = View.VISIBLE
        binding.buttonNo.visibility = View.VISIBLE


        binding.buttonYes.isEnabled = true
        binding.buttonNo.isEnabled = true

        binding.tvQuestion.text = "Чи співпадає колір тексту з назвою?"


        gameEngine.resetGame()
        updateScoreDisplay()
        loadNextRound()
        startGameTimer()
    }

    private fun loadNextRound() {
        val roundData = gameEngine.nextRound()
        binding.tvColorName.text = roundData.leftText
        binding.tvColor.text = roundData.rightText
        binding.tvColor.setTextColor(roundData.rightTextColorValue)
    }

    private fun handleAnswer(userAnswer: Boolean) {
        gameEngine.checkAnswer(userAnswer)
        updateScoreDisplay()
        loadNextRound()
    }

    private fun updateScoreDisplay() {
        binding.progressBar.max = 100
        binding.progressBar.progress = gameEngine.score
        binding.score.text = "Рахунок: ${gameEngine.score}"
    }

    private fun startGameTimer() {
        gameTimer?.cancel()

        gameTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.text = "Час: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                endGame()
            }
        }.start()
    }

    private fun endGame() {
        binding.timer.text = "Час вийшов!"
        binding.tvQuestion.text = "Гру завершено! Рахунок: ${gameEngine.score}"

        binding.buttonYes.visibility = View.GONE
        binding.buttonNo.visibility = View.GONE

        binding.buttonRestart.visibility = View.VISIBLE

        saveGameResults()
    }

    private fun saveGameResults() {
        val emailKey = currentUserEmail ?: "guest"
        val sharedPreferences = getSharedPreferences("Game_Stats", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val scoreKey = "SCORE_" + emailKey

        val oldHighScore = sharedPreferences.getInt(scoreKey, 0)

        if (gameEngine.score > oldHighScore) {
            editor.putInt(scoreKey, gameEngine.score)
            editor.apply()
            Toast.makeText(applicationContext, "Новий рекорд!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun restartGame() {
        startGame()
        binding.buttonRestart.visibility = View.GONE
    }
}