package com.example.pz_3_shynkarenko

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pz_3_shynkarenko.databinding.ResultActivityBinding

class ResultActivity: AppCompatActivity() {
    private lateinit var binding: ResultActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val score = intent.getIntExtra("GAME_SCORE", 0)
        val email = intent.getStringExtra("USER_EMAIL") ?: ""

        binding.tvResultScore.text = "Ваш рахунок: $score"


        binding.buttonSendEmail.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, "Результати гри PZ_3")
                putExtra(Intent.EXTRA_TEXT, "Привіт! Мій результат у грі: $score балів.")
            }

            try {
                startActivity(intent)
            } catch (e: android.content.ActivityNotFoundException) {
                android.widget.Toast.makeText(this, "Поштовий клієнт не знайдено", android.widget.Toast.LENGTH_SHORT).show()
            }

        }

        binding.buttonBackToMenu.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("CURRENT_USER_EMAIL", email)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }


}
