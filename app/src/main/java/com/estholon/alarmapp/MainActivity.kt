package com.estholon.alarmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.estholon.alarmapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // BINDING VARIABLE
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()

    }

    private fun initListeners() {
        binding.btnAdd.setOnClickListener{
            val intent = Intent(this,NewAlarmActivity::class.java)
            startActivity(intent)
        }
    }
}