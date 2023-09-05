package app.appworks.school.stylish.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserTracking(
    val message: String?,
    val status: String?
) : Parcelable
