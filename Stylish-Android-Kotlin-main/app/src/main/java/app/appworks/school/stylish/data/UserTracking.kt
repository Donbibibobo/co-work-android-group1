package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserTracking(
    val message: String?,
    val status: String?,
    val error: String?
) : Parcelable

@Parcelize
data class ChatBoxBack(
    @Json(name = "chat_response") val chatResponse: String?,
    @Json(name = "product_id") val productId: String?,
    @Json(name = "error_message") val errorMessage: String?,
) : Parcelable




