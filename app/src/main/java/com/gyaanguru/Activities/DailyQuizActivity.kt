package com.gyaanguru.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
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
            questionTxt.text = receivedList[position].question ?: "" // Null check

          val drawableResourceId: Int = binding.root.resources.getIdentifier(
              receivedList[position].picPath, "drawable", binding.root.context.packageName)
            if (drawableResourceId != 0) { // Check if resource ID is valid
                val requestOptions = RequestOptions().centerCrop().transform(RoundedCorners(60))
                Glide.with(this@DailyQuizActivity)
                    .load(drawableResourceId)
                    .apply(requestOptions)
                    .into(pic)
            } else {
                // Handle the case where the resource is not found (e.g., show a default image)
                pic.visibility = View.GONE
             //   pic.setImageResource(R.drawable.default_quiz_image)
             //   Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
             //   Log.e("DailyQuizActivity", "Image resource not found for picPath: ${receivedList[position].picPath}")
            }
            loadAnswers()

            rightArrow.setOnClickListener {
                if (progressBar.progress == 10) {
                    val intent = Intent(this@DailyQuizActivity, ScoreActivity::class.java)
                    intent.putExtra("score", allScore)
                    startActivity(intent)
                    finish()
                    return@setOnClickListener
                }
                else if (position < receivedList.size - 1) { // Check if position is within bounds
                    position++
                    progressBar.progress = progressBar.progress + 1
                    questionNumber.text = "Question " + progressBar.progress + "/10"
                    questionTxt.text = receivedList[position].question ?: "" // Null check

                    val drawableResourceId: Int = binding.root.resources.getIdentifier(
                        receivedList[position].picPath, "drawable", binding.root.context.packageName)
                    if (drawableResourceId != 0) { // Check if resource ID is valid
                        val requestOptions = RequestOptions().centerCrop().transform(RoundedCorners(60))
                        Glide.with(this@DailyQuizActivity)
                            .load(drawableResourceId)
                            .apply(requestOptions)
                            .into(pic)
                    } else {
                        // Handle the case where the resource is not found (e.g., show a default image)
                        pic.visibility = View.GONE
                     //   pic.setImageResource(R.drawable.default_quiz_image)
                     //   Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
                     //   Log.e("DailyQuizActivity", "Image resource not found for picPath: ${receivedList[position].picPath}")
                    }
                    loadAnswers()
                }
            }

            leftArrow.setOnClickListener {
                if (progressBar.progress == 1) {
                    return@setOnClickListener
                }
                else if (position > 0) { // Check if position is within bounds
                    position--
                    progressBar.progress = progressBar.progress - 1
                    questionNumber.text = "Question " + progressBar.progress + "/10"
                    questionTxt.text = receivedList[position].question ?: "" // Null check

                    val drawableResourceId: Int = binding.root.resources.getIdentifier(
                        receivedList[position].picPath, "drawable", binding.root.context.packageName)
                    if (drawableResourceId != 0) { // Check if resource ID is valid
                        val requestOptions = RequestOptions().centerCrop().transform(RoundedCorners(60))
                        Glide.with(this@DailyQuizActivity)
                            .load(drawableResourceId)
                            .apply(requestOptions)
                            .into(pic)
                    } else {
                        // Handle the case where the resource is not found (e.g., show a default image)
                        pic.visibility = View.GONE
                     //   pic.setImageResource(R.drawable.default_quiz_image)
                     //   Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
                     //   Log.e("DailyQuizActivity", "Image resource not found for picPath: ${receivedList[position].picPath}")
                    }
                    loadAnswers()
                }
            }
        }
    }

    private fun loadAnswers(){
        val users: MutableList<String> = mutableListOf()
        users.add(receivedList[position].answer_1 ?: "") // Null check
        users.add(receivedList[position].answer_2 ?: "") // Null check
        users.add(receivedList[position].answer_3 ?: "") // Null check
        users.add(receivedList[position].answer_4 ?: "") // Null check

        // Add the clicked answer if it exists (no changes here)
        if (receivedList[position].clickedAnswer != null) {
            users.add(receivedList[position].clickedAnswer.toString())
        }

        val questionAdapter by lazy {
            QuestionAdapter(receivedList[position].correctAnswer ?: "", users, this) // Null check
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