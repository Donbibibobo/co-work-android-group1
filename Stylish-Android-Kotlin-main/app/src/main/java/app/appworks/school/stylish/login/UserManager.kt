package app.appworks.school.stylish.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.data.User
import app.appworks.school.stylish.util.Util.getString

/**
 * Created by Wayne Chen in Jul. 2019.
 */
object UserManager {

    private const val USER_DATA = "user_data"
    private const val USER_TOKEN = "user_token"

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    var userToken: String? = null
        get() = StylishApplication.instance
            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getString(USER_TOKEN, null)
        set(value) {
            field = when (value) {
                null -> {
                    StylishApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .remove(USER_TOKEN)
                        .apply()
                    null
                }
                else -> {
                    StylishApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(USER_TOKEN, value)
                        .apply()
                    value
                }
            }
        }

    /**
     * It can be use to check login status directly
     */
    val isLoggedIn: Boolean
        get() = userToken != null

    /**
     * Clear the [userToken] and the [user]/[_user] data
     */
    fun clear() {
        userToken = null
        _user.value = null
    }

    private var lastChallengeTime: Long = 0
    private var challengeCount: Int = 0
    private const val CHALLENGE_LIMIT = 23

    /**
     * Winter is coming
     */
    fun challenge() {
        if (System.currentTimeMillis() - lastChallengeTime > 5000) {
            lastChallengeTime = System.currentTimeMillis()
            challengeCount = 0
        } else {
            if (challengeCount == CHALLENGE_LIMIT) {
                userToken = null
                Toast.makeText(
                    StylishApplication.instance,
                    getString(R.string.profile_mystic_information),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                challengeCount++
            }
        }
    }
}
