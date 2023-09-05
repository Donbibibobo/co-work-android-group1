package app.appworks.school.stylish.chat

import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {


    fun addMessage(message:String){
        val chatList = mutableListOf<String>()

        chatList.add(message)
    }
}