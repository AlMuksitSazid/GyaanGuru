package com.gyaanguru.Chat_ai

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.gyaanguru.Constants
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = Constants.GEMINI_API_KEY
    )

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    fun sendMessage(question: String){
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(history = messageList.map {content(it.role){text(it.message)}}.toList())
                messageList.add(MessageModel(question, "user"))
                // Handle "who are you" question
                if (question.lowercase().contains("who are you")) {
                    val genericResponse = "I am a Chat AI assistant, here to help you with your questions and requests."
                    messageList.add(MessageModel(genericResponse, "model"))} else {
                    messageList.add(MessageModel("Generating...", "model"))
                    val response = chat.sendMessage(question)
                    messageList.removeLast()
                    messageList.add(MessageModel(response.text.toString(), "model"))
                }
            }catch (e: Exception){
                messageList.removeLast()
                messageList.add(MessageModel("Error: "+e.message.toString(), "model"))
            }
        }
    }
}