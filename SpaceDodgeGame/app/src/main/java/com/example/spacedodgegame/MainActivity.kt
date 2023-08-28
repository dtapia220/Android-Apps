package com.example.spacedodgegame


import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.view.View
import com.google.androidgamesdk.GameActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.widget.ImageView
//import androidx.appcompat.app.AppCompatActivity
import java.util.Random
import android.widget.TextView

class MainActivity : GameActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var spaceship: ImageView
    private lateinit var enemy: ImageView
    private lateinit var timerText: TextView
    private var screenHeight: Int = 0
    private var spaceshipY: Int = 0
    private var enemyX: Float = 0f
    private val handler = Handler()
    private val random = Random()
    private var isMovingUp = false
    private var isGameOver = false
    private var elapsedTime: Int = 0
    private var highScore: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        startTimerUpdateLoop()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.background)
        mediaPlayer.isLooping = true // Loop the music
        mediaPlayer.start()

        // Load the high score from shared preferences
        val prefs = getSharedPreferences("game", Context.MODE_PRIVATE)
        highScore = prefs.getInt("highScore", 0)



        spaceship = findViewById(R.id.spaceship)
        enemy = findViewById(R.id.enemy)
        timerText = findViewById(R.id.timerText)
        screenHeight = resources.displayMetrics.heightPixels
        spaceshipY = screenHeight / 2

        spaceship.y = spaceshipY.toFloat()
        enemyX = screenWidth.toFloat()
        updateEnemyPosition()

        startGameLoop()
    }

    private fun startTimerUpdateLoop() {
        val timerHandler = Handler()
        timerHandler.postDelayed(object : Runnable {
            override fun run() {
                // Update the timerText
                timerText.text = "Time: $elapsedTime seconds"

                // Increment elapsed time
                elapsedTime++

                // Repeat the update every second
                timerHandler.postDelayed(this, 1000)
            }
        }, 1000) // Start the update after 1000ms (1 second)
    }




    private fun startGameLoop() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!isGameOver) {
                    updateGame()
                    startGameLoop()
                }
            }
        }, 16) // Delay by 16ms (approx. 60 FPS)
    }

    private fun updateGame() {
        if (isMovingUp) {
            spaceshipY -= 10 // Move the spaceship upwards
        } else {
            spaceshipY += 10 // Move the spaceship downwards
        }


        // Update high score if needed
        if (elapsedTime > highScore) {
            highScore = elapsedTime
            saveHighScore()
        }

        // Check for collision with screen boundaries
        if (spaceshipY < 0) {
            spaceshipY = 0
        } else if (spaceshipY > screenHeight - spaceship.height) {
            spaceshipY = screenHeight - spaceship.height
        }

        spaceship.y = spaceshipY.toFloat()

        // Update enemy position
        enemyX -= 15 // Move enemy to the left
        if (enemyX < -enemy.width) {
            enemyX = screenWidth.toFloat()
            updateEnemyPosition()
        }
        enemy.x = enemyX


        // Check for collision with enemy
        if (spaceship.x < enemy.x + enemy.width && spaceship.x + spaceship.width > enemy.x &&
            spaceship.y < enemy.y + enemy.height && spaceship.y + spaceship.height > enemy.y
        ) {
            gameOver()
        }
    }

    private fun updateEnemyPosition() {
        val enemyY = random.nextInt(screenHeight - enemy.height)
        enemy.y = enemyY.toFloat()
        enemyX = screenWidth.toFloat()
    }

    private fun gameOver() {
        isGameOver = true
        val intent = Intent(this@MainActivity, GameOverActivity::class.java)
        intent.putExtra("elapsedTime", elapsedTime)
        startActivity(intent)
        finish()
    }

    private fun saveHighScore() {
        val prefs = getSharedPreferences("game", Context.MODE_PRIVATE)
        prefs.edit().putInt("highScore", highScore).apply()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> isMovingUp = true
            MotionEvent.ACTION_UP -> isMovingUp = false
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private val screenWidth: Int
        get() = resources.displayMetrics.widthPixels
}