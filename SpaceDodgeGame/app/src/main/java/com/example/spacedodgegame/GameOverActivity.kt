package com.example.spacedodgegame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.spacedodgegame.MainActivity

import android.view.View
import com.google.androidgamesdk.GameActivity

import android.os.Handler
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import java.util.Random

class GameOverActivity : GameActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        val elapsedTime = intent.getIntExtra("elapsedTime", 0)

        val elapsedTimeText: TextView = findViewById(R.id.elapsedTimeText)

        // Retrieve the high score
        val prefs = getSharedPreferences("game", Context.MODE_PRIVATE)
        val savedHighScore = prefs.getInt("highScore", 0)

        // Display the high score
        val highScoreText: TextView = findViewById(R.id.highScoreText)
        highScoreText.text = "High Score: $savedHighScore"

        elapsedTimeText.text = "You survived for: $elapsedTime seconds"
        val restartButton: Button = findViewById(R.id.restartButton)
        restartButton.setOnClickListener {
            val intent = Intent(this@GameOverActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}