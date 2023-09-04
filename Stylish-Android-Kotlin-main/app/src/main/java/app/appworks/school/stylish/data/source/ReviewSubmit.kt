package app.appworks.school.stylish.data.source

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewSubmit(
    val userID: String,
    val product_id: String,
    val review: String,
    val timestamp: String,
    val version: String
) : Parcelable
