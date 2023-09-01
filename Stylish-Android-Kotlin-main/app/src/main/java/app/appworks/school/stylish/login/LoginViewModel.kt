package app.appworks.school.stylish.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.stylish.R
import app.appworks.school.stylish.data.Result
import app.appworks.school.stylish.data.User
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.network.LoadApiStatus
import app.appworks.school.stylish.util.Logger
import app.appworks.school.stylish.util.Util.getString
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [LoginDialog].
 */
class LoginViewModel(private val stylishRepository: StylishRepository) : ViewModel() {

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    // Handle navigation to login success
    private val _navigateToLoginSuccess = MutableLiveData<User>()

    val navigateToLoginSuccess: LiveData<User>
        get() = _navigateToLoginSuccess

    // Handle leave login
    private val _loginFacebook = MutableLiveData<Boolean>()

    val loginFacebook: LiveData<Boolean>
        get() = _loginFacebook

    // Handle leave login
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    val email = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    private val _invalidCheckout = MutableLiveData<Int>()

    val invalidCheckout: LiveData<Int>
        get() = _invalidCheckout

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    lateinit var fbCallbackManager: CallbackManager

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]$this")
        Logger.i("------------------------------------")
    }

    /**
     * track [StylishRepository.userSignIn]: -> [DefaultStylishRepository] : [StylishRepository] -> [StylishRemoteDataSource] : [StylishDataSource]
     * @param fbToken: Facebook token
     */
    private fun loginStylish(fbToken: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            // It will return Result object after Deferred flow
            when (val result = stylishRepository.userSignIn(fbToken)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    UserManager.userToken = result.data.userSignIn?.accessToken
                    _user.value = result.data.userSignIn?.user
                    _navigateToLoginSuccess.value = user.value
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    private fun loginStylish(email: String, password: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            // It will return Result object after Deferred flow
            when (val result = stylishRepository.userSignIn(email, password)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    UserManager.userToken = result.data.userSignIn?.accessToken
                    _user.value = result.data.userSignIn?.user
                    _navigateToLoginSuccess.value = user.value
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    /**
     * Login Stylish by Facebook: Step 1. Register FB Login Callback
     */
    fun login() {
        _status.value = LoadApiStatus.LOADING

        fbCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(
            fbCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {

                    loginStylish(loginResult.accessToken.token)
                }

                override fun onCancel() { _status.value = LoadApiStatus.ERROR }

                override fun onError(exception: FacebookException) {
                    Logger.w("[${this::class.simpleName}] exception=${exception.message}")

                    exception.message?.let {
                        _error.value = if (it.contains("ERR_INTERNET_DISCONNECTED")) {
                            getString(R.string.internet_not_connected)
                        } else {
                            it
                        }
                    }
                    _status.value = LoadApiStatus.ERROR
                }
            }
        )

        loginFacebook()
    }

    fun nativeLogin() {

        if (email.value.isNullOrEmpty()) {
            _error.value = getString(R.string.email_cannot_be_empty)
            return
        } else if (password.value.isNullOrEmpty()) {
            _error.value = getString(R.string.password_cannot_be_empty)
            return
        }

        loginStylish(email.value ?: "", password.value ?: "")
    }

    /**
     * Login Stylish by Facebook: Step 2. Login Facebook
     */
    private fun loginFacebook() {
        _loginFacebook.value = true
    }

    fun leave() {
        _leave.value = true
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }

    fun nothing() {}

    fun onLoginFacebookCompleted() {
        _loginFacebook.value = null
    }
}
