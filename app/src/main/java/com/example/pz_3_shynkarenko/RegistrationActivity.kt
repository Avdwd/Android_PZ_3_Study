package com.example.pz_3_shynkarenko

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.pz_3_shynkarenko.databinding.RegistrationActivityBinding

class RegistrationActivity: AppCompatActivity() {
    private lateinit var binding: RegistrationActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistrationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegistration.setOnClickListener{
            registration()

        }
        binding.buttonLogin.setOnClickListener {
            login()

        }

    }


    private fun registration(){
        val email = binding.editTextTextEmailAddress.text.toString()
        val password = binding.editTextTextPassword.text.toString()
        val sharedPreferences = getSharedPreferences("User_Data", MODE_PRIVATE)

        val intent = Intent(this, MainActivity::class.java)

        val editor = sharedPreferences.edit()

        if(email.isNotEmpty()&& password.isNotEmpty()){
            editor.putString("KEY_EMAIL", email)
            editor.putString("KEY_PASSWORD", password)
            editor.apply()
            intent.putExtra("CURRENT_USER_EMAIL", email)
            startActivity(intent)
        }else{
            Toast.makeText(this,"Введіть дані в поля", Toast.LENGTH_SHORT).show()
        }

    }

    private fun login(){
        val email = binding.editTextTextEmailAddress.text.toString()
        val password = binding.editTextTextPassword.text.toString()
        val sharedPreferences = getSharedPreferences("User_Data", MODE_PRIVATE)

        val intent = Intent(this, MainActivity::class.java)

        val savedEmail = sharedPreferences.getString("KEY_EMAIL", "")
        val savedPassword = sharedPreferences.getString("KEY_PASSWORD", "")

        if(email == savedEmail && savedPassword == password){
            intent.putExtra("CURRENT_USER_EMAIL", email)
            startActivity(intent)
        }
        else{
            Toast.makeText(this,"Введіть дані в поля", Toast.LENGTH_SHORT).show()
        }

    }



}