package app.appworks.school.stylish.chat

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

class ChatViewModel : ViewModel() {

    var bitmap =  MutableLiveData<Bitmap>()

    var path: String = ""

    fun addImage(){
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

}