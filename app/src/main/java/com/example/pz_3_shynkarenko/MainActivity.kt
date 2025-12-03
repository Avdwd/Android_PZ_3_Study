package com.example.pz_3_shynkarenko

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pz_3_shynkarenko.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val gameEngine = GameEngine()
    private var gameTimer: CountDownTimer? = null
    private var currentUserEmail: String? = null

    private var count = 0
    private var timeLeftInMillis: Long = 60000
    private var isGameRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserEmail = intent.getStringExtra("CURRENT_USER_EMAIL")

        binding.buttonYes.setOnClickListener { handleAnswer(true) }
        binding.buttonNo.setOnClickListener { handleAnswer(false) }

        binding.buttonStart.setOnClickListener {
            startGame()
        }


        if (savedInstanceState == null) {
            setupInitialScreen()
        }
    }

    private fun setupInitialScreen() {
        binding.buttonStart.visibility = View.VISIBLE
        binding.buttonYes.visibility = View.GONE
        binding.buttonNo.visibility = View.GONE


        binding.tvQuestion.text = "Натисніть Старт, щоб почати"
        binding.timer.text = "Час: 60"
        binding.score.text = "Рахунок: 0"

        binding.tvColorName.text = ""
        binding.tvColor.text = ""
    }

    private fun startGame() {
        isGameRunning = true

        setupGameUI()

        gameEngine.resetGame()
        updateScoreDisplay()
        loadNextRound()

        startTimer(60000)
    }


    private fun setupGameUI() {
        binding.buttonStart.visibility = View.GONE
        binding.buttonYes.visibility = View.VISIBLE
        binding.buttonNo.visibility = View.VISIBLE
        binding.buttonYes.isEnabled = true
        binding.buttonNo.isEnabled = true
        binding.tvQuestion.text = "Чи співпадає колір тексту з назвою?"
    }

    private fun loadNextRound() {
        val roundData = gameEngine.nextRound()
        binding.tvColorName.text = roundData.leftText
        binding.tvColor.text = roundData.rightText
        binding.tvColor.setTextColor(roundData.rightTextColorValue)
        count++
    }

    private fun handleAnswer(userAnswer: Boolean) {
        gameEngine.checkAnswer(userAnswer)
        updateScoreDisplay()
        loadNextRound()
    }

    private fun updateScoreDisplay() {
        binding.progressBar.max = count
        binding.progressBar.progress = gameEngine.score
        binding.score.text = "Рахунок: ${gameEngine.score}"
    }


    private fun startTimer(timeInMillis: Long) {
        gameTimer?.cancel()

        gameTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                binding.timer.text = "Час: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                isGameRunning = false
                endGame()
            }
        }.start()
    }

    private fun endGame() {

        gameTimer?.cancel()


        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("GAME_SCORE", gameEngine.score)
        intent.putExtra("USER_EMAIL", currentUserEmail)

        startActivity(intent)
        finish()
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("KEY_SCORE", gameEngine.score)
        outState.putInt("KEY_TOTAL_ANSWERS", gameEngine.totalAnswers)
        outState.putLong("KEY_TIME_LEFT", timeLeftInMillis)
        outState.putBoolean("KEY_IS_RUNNING", isGameRunning)
        outState.putInt("KEY_COUNT", count)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)


        val savedScore = savedInstanceState.getInt("KEY_SCORE", 0)
        val savedTotal = savedInstanceState.getInt("KEY_TOTAL_ANSWERS", 0)
        timeLeftInMillis = savedInstanceState.getLong("KEY_TIME_LEFT", 60000)
        isGameRunning = savedInstanceState.getBoolean("KEY_IS_RUNNING", false)
        count = savedInstanceState.getInt("KEY_COUNT", 0)

        gameEngine.restoreState(savedScore, savedTotal)

        updateScoreDisplay()

        if (isGameRunning) {
            setupGameUI()
            startTimer(timeLeftInMillis)
            loadNextRound()
        } else {
            setupInitialScreen()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameTimer?.cancel()
    }
}