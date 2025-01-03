package com.example.myapplication1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color


class MainActivity : AppCompatActivity() {
    private val boardSize = 3
    private var currentPlayer = "X"
    private val board = Array(boardSize) { Array(boardSize) { "" } }
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val statusTextView = findViewById<TextView>(R.id.statusTextView)
        val playAgainButton = findViewById<Button>(R.id.playAgainButton)

        // Initialize Edge-to-Edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize board
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                board[i][j] = ""
            }
        }

        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                val textView = TextView(this).apply {
                    text = ""
                    setBackgroundColor(Color.LTGRAY)
                    textSize = 32f
                    gravity = android.view.Gravity.CENTER
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 300  // גודל הריבוע ברוחב
                        height = 300 // גובה הריבוע
                        rowSpec = GridLayout.spec(i, 1f)
                        columnSpec = GridLayout.spec(j, 1f)
                        setMargins(8, 8, 8, 8) // מרווחים בין הריבועים
                    }
                }

                // OnClickListener לשינוי הטקסט בתוך הריבוע
                textView.setOnClickListener {
                    if (!gameOver && textView.text == "") {
                        textView.text = currentPlayer
                        board[i][j] = currentPlayer
                        if (checkWinner()) {
                            statusTextView.text = "Player $currentPlayer wins!"
                            gameOver = true
                        } else if (isBoardFull()) {
                            statusTextView.text = "It's a draw!"
                            gameOver = true
                        } else {
                            currentPlayer = if (currentPlayer == "X") "O" else "X"
                            statusTextView.text = "Fun Game"
                        }
                    }
                }

                gridLayout.addView(textView)
            }
        }



        // Play Again Button
        playAgainButton.setOnClickListener {
            resetGame(gridLayout, statusTextView)
        }
    }

    private fun resetGame(gridLayout: GridLayout, statusTextView: TextView) {
        gameOver = false
        currentPlayer = "X"
        statusTextView.text = "Fun Game"
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as TextView
            button.text = ""
        }
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                board[i][j] = ""
            }
        }
    }

    private fun checkWinner(): Boolean {
        for (i in 0 until boardSize) {
            // Check rows and columns
            if (board[i].all { it == currentPlayer } || board.map { it[i] }.all { it == currentPlayer }) return true
        }
        // Check diagonals
        if ((0 until boardSize).all { board[it][it] == currentPlayer }) return true
        if ((0 until boardSize).all { board[it][boardSize - it - 1] == currentPlayer }) return true
        return false
    }

    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { it != "" } }
    }
}
