package com.gyaanguru.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyaanguru.Adapter.QuestionAdapter
import com.gyaanguru.Domain.QuestionModel
import com.gyaanguru.databinding.ActivityDailyQuizBinding

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gyaanguru.Domain.questionsList
import com.gyaanguru.R

class DailyQuizActivity : AppCompatActivity(), QuestionAdapter.score {

    private lateinit var binding: ActivityDailyQuizBinding
    var position: Int = 0
    var receivedList: MutableList<QuestionModel> = mutableListOf()
    var allScore = 0

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDailyQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val window: Window =this@DailyQuizActivity.window
        window.statusBarColor= ContextCompat.getColor(this@DailyQuizActivity, R.color.dark_pink)

        receivedList = ArrayList(questionsList()).toMutableList()
    //    receivedList = intent.getParcelableArrayListExtra<QuestionModel>("list")!!.toMutableList()

        binding.apply {
            backImageView.setOnClickListener { finish() }

            progressBar.progress = 1
            questionTxt.text = receivedList[position].question

          val drawableResourceId: Int = binding.root.resources.getIdentifier(
              receivedList[position].picPath, "drawable", binding.root.context.packageName)
          val requestOptions = RequestOptions().centerCrop().transform(RoundedCorners(60))
            Glide.with(this@DailyQuizActivity)
                .load(drawableResourceId)
                .apply(requestOptions)
                .into(pic)
            loadAnswers()

            rightArrow.setOnClickListener {
                if (progressBar.progress == 10) {
                    val intent = Intent(this@DailyQuizActivity, ScoreActivity::class.java)
                    intent.putExtra("score", allScore)
                    startActivity(intent)
                    finish()
                    return@setOnClickListener
                }
                position++
                progressBar.progress = progressBar.progress + 1
                questionNumber.text = "Question " + progressBar.progress + "/10"
                questionTxt.text = receivedList[position].question

            val drawableResourceId: Int = binding.root.resources.getIdentifier(
                 receivedList[position].picPath, "drawable", binding.root.context.packageName)
            val requestOptions = RequestOptions().centerCrop().transform(RoundedCorners(60))
            Glide.with(this@DailyQuizActivity)
                .load(drawableResourceId)
                .apply(requestOptions)
                .into(pic)
            loadAnswers()
            }

            leftArrow.setOnClickListener {
                if (progressBar.progress == 1) {
                    return@setOnClickListener
                }
                position--
                progressBar.progress = progressBar.progress - 1
                questionNumber.text = "Question " + progressBar.progress + "/10"
                questionTxt.text = receivedList[position].question

                val drawableResourceId: Int = binding.root.resources.getIdentifier(
                    receivedList[position].picPath, "drawable", binding.root.context.packageName)
                val requestOptions = RequestOptions().centerCrop().transform(RoundedCorners(60))
                Glide.with(this@DailyQuizActivity)
                    .load(drawableResourceId)
                    .apply(requestOptions)
                    .into(pic)
                loadAnswers()
            }
        }
    }

    private fun loadAnswers(){
        val users: MutableList<String> = mutableListOf()
        users.add(receivedList[position].answer_1.toString())
        users.add(receivedList[position].answer_2.toString())
        users.add(receivedList[position].answer_3.toString())
        users.add(receivedList[position].answer_4.toString())

        if (receivedList[position].clickedAnswer != null) users.add(receivedList[position].clickedAnswer.toString())

        val questionAdapter by lazy {
            QuestionAdapter(receivedList[position].correctAnswer.toString(), users, this)
        }

        questionAdapter.differ.submitList(users)
        binding.questionList.apply { layoutManager = LinearLayoutManager(this@DailyQuizActivity)
            adapter = questionAdapter
        }
    }

    override fun amount(number: Int, clickedAnswer: String) {
        allScore += number
        receivedList[position].clickedAnswer = clickedAnswer
    }
}