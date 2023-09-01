package app.appworks.school.stylish.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Wayne Chen in Jul. 2019.
 */
@Parcelize
data class NativeSignInBody(
    val provider: String = "native",
    val email: String,
    val password: String
) : Parcelable
