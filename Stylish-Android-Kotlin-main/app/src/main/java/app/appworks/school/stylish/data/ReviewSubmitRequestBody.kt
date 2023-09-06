package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class ReviewSubmitRequestBody(
    val userID: String,
    val product_id: Long,
    val review: String,
    val timestamp: String,
    val version: String
)
