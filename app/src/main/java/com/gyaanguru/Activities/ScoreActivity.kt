package com.gyaanguru.Activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gyaanguru.R
import com.gyaanguru.databinding.ActivityScoreBinding

class ScoreActivity : AppCompatActivity() {

    var score: Int = 0
    lateinit var binding: ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val window: Window =this@ScoreActivity.window
        window.statusBarColor= ContextCompat.getColor(this@ScoreActivity, R.color.dark_pink)

        score = intent.getIntExtra("score", 0)

        binding.apply {
            scoreTxt.text = score.toString()
            backImageView.setOnClickListener { finish() }
            homeButton.setOnClickListener {
                startActivity(Intent(this@ScoreActivity, MainActivity::class.java))
                finish()
            }
        }

    }
}