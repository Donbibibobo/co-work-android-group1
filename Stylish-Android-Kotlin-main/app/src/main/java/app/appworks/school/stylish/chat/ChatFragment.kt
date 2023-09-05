package app.appworks.school.stylish.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.appworks.school.stylish.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_chat_gpt, container, false)
        val binding = FragmentChatBinding.inflate(inflater)

        val input = listOf(binding.gptInputText.text.toString())
        val adapter = ChatAdapter()

        val chatList = mutableListOf<ChatDataClass>()

        val sen1 = ChatDataClass.Sent("Who are you?")
        val received1 = ChatDataClass.Received("I'm chat!")

        chatList.add(sen1)
        chatList.add(received1)

        binding.gptRecyclerView.adapter = adapter

        binding.sendButton.setOnClickListener {
            adapter.submitList(chatList)
        }




        return binding.root
    }
}