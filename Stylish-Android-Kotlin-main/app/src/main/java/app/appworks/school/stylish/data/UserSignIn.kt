package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * Created by Wayne Chen in Jul. 2019.
 */
@Parcelize
data class UserSignIn(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "access_expired") val accessExpired: Int,
    val user: User
) : Parcelable
