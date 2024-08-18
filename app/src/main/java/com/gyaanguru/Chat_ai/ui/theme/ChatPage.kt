package com.gyaanguru.Chat_ai.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gyaanguru.Chat_ai.ChatViewModel
import com.gyaanguru.Chat_ai.MessageModel
import com.gyaanguru.R

@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel = ChatViewModel()){
    Column(modifier = modifier.fillMaxSize().background(LightGreen)) {
        MessageList(modifier = Modifier.weight(1f), messageList = viewModel.messageList)
        MessageInput(onMessageSend = { viewModel.sendMessage(it) })
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>){
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween){
        if (messageList.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(painter = painterResource(id = R.drawable.technical_support), modifier = Modifier.size(60.dp), tint = Color.Unspecified, contentDescription = "Icon")
                Text(text = "Ask me anything?", fontSize = 22.sp)
            }
        }else{
            LazyColumn(modifier = modifier, reverseLayout = true){ items(messageList.reversed()){ MessageRow(messageModel = it) } }
        }
    }
}

@Composable
fun MessageRow(messageModel: MessageModel){
    val isModel = messageModel.role == "model"
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.fillMaxWidth()){
            Box(modifier = Modifier
                .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                .padding(
                    start = if (isModel) 8.dp else 70.dp,
                    end = if (isModel) 70.dp else 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .clip(RoundedCornerShape(20f))
                .background(if (isModel) ColorModelMessage else ColorUserMessage)
                .padding(10.dp)
            ) { SelectionContainer { Text(text = messageModel.message, fontWeight = FontWeight.W500, textAlign = TextAlign.Justify, color = Color.White) } }
        }
    }
}

@Composable
fun MessageInput(onMessageSend: (String) -> Unit){
    var message by remember { mutableStateOf("") }
    Row(modifier = Modifier.padding(8.dp).padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically){
        OutlinedTextField(value = message, onValueChange = {message = it}, modifier = Modifier.weight(1f).background(Color.LightGray), placeholder = { Text("Ask a question?") } )
        IconButton(onClick = { if(message.isNotEmpty()){
            onMessageSend(message)
            message = "" } }) { Icon(imageVector = Icons.Default.Send, tint = Color.Blue, contentDescription = "Send") }
    }
}