package app.appworks.school.stylish.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateDpAsState
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.canTakeScreenshot
import app.appworks.school.stylish.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        binding.viewModel = viewModel

        var input = binding.chatInputText.text
        val adapter = ChatAdapter()
        val chatList = mutableListOf<ChatDataClass>()

        binding.gptRecyclerView.adapter = adapter

        binding.sendButton.setOnClickListener {
            chatList.add(ChatDataClass.Sent(input.toString()))
            adapter.submitList(chatList)
            adapter.notifyDataSetChanged()
            viewModel.sendToChatGpt(input.toString())
        }


        viewModel.gptResponse.observe(viewLifecycleOwner, Observer {
            chatList.add(ChatDataClass.Received(it))
//            chatList.add(ChatDataClass.Img("aa"))
            adapter.submitList(chatList)
            Log.i("CHAT1",chatList.toString())
            Log.i("CHAT1",it)
            adapter.notifyDataSetChanged()
        })

        return binding.root
    }
}