package com.gyaanguru.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.gyaanguru.Chat_ai.ChatViewModel
import com.gyaanguru.Chat_ai.ui.theme.ChatPage

class ChataiFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply { setContent { ChatPage(viewModel = viewModel()) } }
    }

    @Composable
    fun viewModel(): ChatViewModel {
        // Initialize and return your ChatViewModel here
        return ViewModelProvider(this)[ChatViewModel::class.java]
    }
}