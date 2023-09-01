package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * Created by Wayne Chen in Jul. 2019.
 */
@Parcelize
data class UserSignUpResult(
    val error: String? = null,
    @Json(name = "data") val userSignIn: UserSignUp? = null
) : Parcelable
