package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Created by Wayne Chen in Jul. 2019.
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class Color(
    val name: String,
    val code: String
) : Parcelable
