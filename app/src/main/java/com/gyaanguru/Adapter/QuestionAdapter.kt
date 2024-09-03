package com.gyaanguru.Adapter

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gyaanguru.R
import com.gyaanguru.databinding.ViewholderQuestionBinding

data class QuestionAdapter(val correctAnswer: String,
                           val users: MutableList<String> = mutableListOf(),
                           var returnScore: score) : RecyclerView.Adapter<QuestionAdapter.Viewholder>() {

    interface score {
        fun amount(number: Int, clickedAnswer: String)
    }

    private lateinit var binding: ViewholderQuestionBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ViewholderQuestionBinding.inflate(inflater,parent, false)
        return Viewholder()
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val binding = ViewholderQuestionBinding.bind(holder.itemView)
        binding.answerTxt.text = differ.currentList[position]

        val correctAnswerLetter = correctAnswer

        // Reset backgrounds to default
        binding.answerTxt.setBackgroundResource(R.drawable.white_bg)
        binding.answerTxt.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
        binding.answerTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)

        if (differ.currentList.size == 5) {
            val clickedAnswerLetter = differ.currentList[4]

            if (clickedAnswerLetter in "abcd" && correctAnswerLetter in "abcd") {
                val clickedPosition = when (clickedAnswerLetter) {
                    "a" -> 0
                    "b" -> 1
                    "c" -> 2
                    "d" -> 3
                    else -> -1 // Handle invalid input
                }

                if (clickedPosition != -1) {
                    // Mark the selected answer (correct or incorrect)
                    if (position == clickedPosition) {
                        if (clickedAnswerLetter == correctAnswerLetter) {
                            // Correct answer selected
                            binding.answerTxt.setBackgroundResource(R.drawable.green_bg)
                            binding.answerTxt.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
                            binding.answerTxt.setTypeface(null, Typeface.BOLD) // Set text to bold
                            val drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.tick)
                            binding.answerTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
                        } else {
                            // Incorrect answer selected
                            binding.answerTxt.setBackgroundResource(R.drawable.red_bg)
                            binding.answerTxt.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
                            binding.answerTxt.setTypeface(null, Typeface.BOLD) // Set text to bold
                            val drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.thieves)
                            binding.answerTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
                        }
                    }

                    // Highlight the correct answer (if it's different from the selected answer)
                    if (clickedAnswerLetter != correctAnswerLetter) {
                        val correctPosition = when (correctAnswerLetter) {
                            "a" -> 0
                            "b" -> 1
                            "c" -> 2
                            "d" -> 3
                            else -> -1
                        }
                        if (correctPosition != -1 && position == correctPosition) {
                            binding.answerTxt.setBackgroundResource(R.drawable.yellow_bg)
                            binding.answerTxt.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
                            binding.answerTxt.setTypeface(null, Typeface.BOLD) // Set text to bold
                            val correctDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.tick)
                            binding.answerTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, correctDrawable, null)
                        }
                    }
                } else {
                    Log.e("QuestionAdapter", "Invalid clickedAnswerLetter: $clickedAnswerLetter")
                }
            } else {
                // Handle the case where either clickedAnswerLetter or correctAnswerLetter is invalid
                Log.e("QuestionAdapter", "Invalid answer format: clickedAnswerLetter = $clickedAnswerLetter, correctAnswerLetter = $correctAnswerLetter")
            }
        }

        // Hide the clicked answer item
        if (position == 4) {
            binding.root.visibility = View.GONE
        }

        // Handle answer clicks
        holder.itemView.setOnClickListener {
            if (differ.currentList.size < 5) {
                val clickedAnswerLetter = when (position) {
                    0 -> "a"
                    1 -> "b"
                    2 -> "c"
                    3 -> "d"
                    else -> ""
                }

                users.add(clickedAnswerLetter)
                notifyDataSetChanged() // Update the entire list

                if (clickedAnswerLetter == correctAnswerLetter) {
                    returnScore.amount(1, clickedAnswerLetter)
                } else {
                    returnScore.amount(0, clickedAnswerLetter)
                }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    inner class Viewholder : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}