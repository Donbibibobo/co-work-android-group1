package app.appworks.school.stylish.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.appworks.school.stylish.data.ChatBoxAPI
import app.appworks.school.stylish.data.ChatBoxBack
import app.appworks.school.stylish.network.UserStylishApi
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {


    private var _isAvailableToSend = true

    private var _gptResponse = MutableLiveData<String>()
    val gptResponse: LiveData<String>
        get() = _gptResponse

    fun sendToChatGpt(message: String) {

        if (_isAvailableToSend) {
            viewModelScope.launch {
                try {
                    val request = ChatBoxAPI(message)
                    val response = UserStylishApi.retrofitService.userTrackingChat(request)
                    _gptResponse.value = response.chatResponse
                    Log.i("CHAT", "_gptResponse.value: ${_gptResponse.value}")

                } catch (e: Exception) {
                    Log.i("CHAT", "${e.message}")
                }
            }
            _isAvailableToSend = false
        }
    }

//    fun getChatGpt() {
//        if (!_isAvailableToSend) {
//            viewModelScope.launch {
//                try {
//                    val response = UserStylishApi.retrofitService.getGptResponse(
//                        productId = "product_id",
//                        chatResponse = "chat_response",
//                        errorMessage = "error_message"
//                    )
//                    _gptResponse = listOf(response)
//                } catch (e: Exception) {
//                    Log.i("CHAT", "${e.message}")
//                }
//            }
//            _isAvailableToSend = true
//        }
//    }
}