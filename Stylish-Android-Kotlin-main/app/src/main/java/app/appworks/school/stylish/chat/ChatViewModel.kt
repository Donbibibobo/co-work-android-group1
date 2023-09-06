package app.appworks.school.stylish.chat

import app.appworks.school.stylish.data.ChatBoxAPI
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.appworks.school.stylish.network.UserStylishApi
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ChatViewModel : ViewModel() {

    var bitmap = MutableLiveData<Bitmap>()

    var path = MutableLiveData<String>()

    var chatImgResponse = MutableLiveData<String?>()


    fun addImage(pathOK: String) {
        val file = File(pathOK)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        viewModelScope.launch {
            try {
                val response = UserStylishApi.retrofitService.userTrackingChatImage(body)
                chatImgResponse.value = response.chatResponse

                Log.i("chatImgResponse", "[chatImgResponse]: ${response.chatResponse}")
                Log.i("chatImgResponse", "[chatImgResponse]: ${response.errorMessage}")
                Log.i("chatImgResponse", "[chatImgResponse]: $response")

            }catch (e: Exception) {
                Log.i("chatImgResponse", "[chatImgResponse]: $e")
                chatImgResponse.value = "Please upload the image again!"
            }

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


    fun getDetail(productId: String) {
        viewModelScope.launch{
            Log.i("getDetail", "called")
            val detailProduct = UserStylishApi.retrofitService.getDetailProduct(productId.toLong())
            Log.i("getDetail", "$detailProduct")
        }
    }
}
