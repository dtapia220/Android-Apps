package com.example.spacedodgegame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val startButton = findViewById<View>(R.id.startButton)
        val highScoresButton = findViewById<View>(R.id.highScoresButton)
        val settingsButton = findViewById<View>(R.id.settingsButton)
        val exitButton = findViewById<View>(R.id.exitButton)

        startButton.setOnClickListener {
            Log.d("MainMenuActivity", "Start Game button clicked")
            startActivity(Intent(this, MainActivity::class.java))
        }

        highScoresButton.setOnClickListener {
            // Implement high scores screen or leaderboard activity
        }

        settingsButton.setOnClickListener {
            // Implement settings activity
        }

        exitButton.setOnClickListener {
            finishAffinity() // Close all activities and exit
        }
    }
}