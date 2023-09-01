package app.appworks.school.stylish.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.stylish.R
import app.appworks.school.stylish.component.ProfileAvatarOutlineProvider
import app.appworks.school.stylish.data.Result
import app.appworks.school.stylish.data.User
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.network.LoadApiStatus
import app.appworks.school.stylish.util.Logger
import app.appworks.school.stylish.util.Util.getString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [ProfileFragment].
 */
class ProfileViewModel(
    private val stylishRepository: StylishRepository,
    private val arguments: User?
) : ViewModel() {

    // After login to Stylish server through Facebook, at the same time we can get user info to provide to display ui
    private val _user = MutableLiveData<User>().apply {
        arguments?.let {
            value = it
        }
    }

    val user: LiveData<User>
        get() = _user

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val outlineProvider = ProfileAvatarOutlineProvider()

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Get [User] profile data when user is null
     */
    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]$this")
        Logger.i("------------------------------------")

        if (user.value == null) {
            UserManager.userToken?.let {
                getUserProfile(it)
            }
        }
    }

    /**
     * track [StylishRepository.getUserProfile]: -> [DefaultStylishRepository] : [StylishRepository] -> [StylishRemoteDataSource] : [StylishDataSource]
     * @param token: Stylish token
     */
    private fun getUserProfile(token: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = stylishRepository.getUserProfile(token)

            _user.value = when (result) {

                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    if (result.error.contains("Invalid Access Token", true)) {
                        UserManager.clear()
                    }
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
        }
    }

    /**
     * No one knows
     */
    fun challenge() = UserManager.challenge()
}
