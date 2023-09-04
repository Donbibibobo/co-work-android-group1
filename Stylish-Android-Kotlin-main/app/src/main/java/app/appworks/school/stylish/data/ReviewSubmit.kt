package app.appworks.school.stylish.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewSubmit(
    val message: String?
) : Parcelable
