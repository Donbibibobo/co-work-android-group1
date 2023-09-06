package app.appworks.school.stylish.chat

import app.appworks.school.stylish.data.ChatBoxAPI
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.appworks.school.stylish.data.UserTrackingRequestBodyString
import app.appworks.school.stylish.network.UserStylishApi
import app.appworks.school.stylish.util.ABtest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import app.appworks.school.stylish.data.ChatBoxBack
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    var bitmap = MutableLiveData<Bitmap>()

    var path: String = ""

    fun addImage() {
        val file = File(path)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        viewModelScope.launch {
            val response = UserStylishApi.retrofitService.userTrackingChatImage(body)

            Log.i("userTracking", "[login]: ${response.chatResponse}")
            Log.i("userTracking", "[login]: ${response.errorMessage}")
            Log.i("userTracking", "[login_content]: $response")
        }
    }

    // J
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
                    _gptResponse.value = response.chatResponse!!
                    Log.i("CHAT", "_gptResponse.value: ${_gptResponse.value}")

                } catch (e: Exception) {
                    Log.i("CHAT", "${e.message}")
                }
            }
            _isAvailableToSend = false
        }
    }
}
