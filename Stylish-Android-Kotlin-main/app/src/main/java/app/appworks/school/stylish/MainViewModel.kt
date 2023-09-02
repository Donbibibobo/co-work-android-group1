package app.appworks.school.stylish

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import app.appworks.school.stylish.component.ProfileAvatarOutlineProvider
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.Result
import app.appworks.school.stylish.data.User
import app.appworks.school.stylish.data.UserTrackingRequestBody
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.network.LoadApiStatus
import app.appworks.school.stylish.network.UserStylishApi
import app.appworks.school.stylish.util.ABtest
import app.appworks.school.stylish.util.CurrentFragmentType
import app.appworks.school.stylish.util.DrawerToggleType
import app.appworks.school.stylish.util.Logger
import app.appworks.school.stylish.util.Util.getString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import kotlin.random.Random

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [MainActivity].
 */
class MainViewModel(private val stylishRepository: StylishRepository, private val application: Application) : AndroidViewModel(application) {


    // use live data to avoid [SharedPreferences & ABtest which create faster]
    private val _version = MutableLiveData<String>()
    val version: LiveData<String>
        get() = _version

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _version

    fun abTest() {
        Log.i("ABtest", "MainViewModel called")
        val pref = application.getSharedPreferences("ABtest", Context.MODE_PRIVATE)
        val editor = pref.edit()

        val isVersionExist : String? = pref.getString("ABtest_Version", "noVersion")

        if (isVersionExist == "noVersion"){
            Log.i("ABtest", "if called")

            // user version
            val versionA = "A"
            val versionB = "B"

            // create a random number 0 & 1
            val randomChoice = Random.nextInt(0, 2)

            // create a random version A & B
            val selectedVersion = if (randomChoice == 0) versionA else versionB

            editor.putString("ABtest_Version", selectedVersion)

            ABtest.version = selectedVersion
            _version.value = selectedVersion

            Log.i("ABtest", "ABtest.version: ${ABtest.version}")

        // user id
            val userId = UUID.randomUUID().toString()

            editor.putString("user_id", userId)

            ABtest.userId = userId

            editor.apply()

            Log.i("ABtest", "ABtest.userId: ${ABtest.userId}")


        } else {
            Log.i("ABtest", "else called")

            // user version
            ABtest.version = isVersionExist.toString()
            _version.value = isVersionExist.toString()

            Log.i("ABtest", "ABtest.version: ${ABtest.version}")

        // user id
            val isIdExist : String? = pref.getString("user_id", "noId")
            if (isIdExist == "noId") {throw IllegalArgumentException("MainViewModel: has version but no id")}

            ABtest.userId = isIdExist.toString()
            _userId.value = isIdExist.toString()

            Log.i("ABtest", "ABtest.userId: ${ABtest.userId}")

        }
    }

    init {
        abTest()
    }

    // user tracking: login
    fun userTrackOpenApp(){
        viewModelScope.launch {
        //
            val eventDetail = JSONObject()
            val checkoutItemArray = JSONArray()

            checkoutItemArray.put("ada1212")
            checkoutItemArray.put("asd231231")
            checkoutItemArray.put("asdasd444")

            eventDetail.put("checkout_item", checkoutItemArray)

            Log.i("ABtest", "eventDetail: ${eventDetail.toString()}")
            Log.i("ABtest", "currentDateTime: ${ABtest.getCurrentDateTime()}")

//            UserStylishApi.retrofitService.userTracking(ABtest.userId, "login", eventDetail.toString(), ABtest.getCurrentDateTime(), ABtest.version)


//            val request = UserTrackingRequestBody("UUID", "login", eventDetail,"2023/09/02", "A")
//            UserStylishApi.retrofitService.userTracking2(request)

        }
    }

























    // user: MainViewModel has User info to provide Drawer UI
    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    // products: Get products from database to provide count number to bottom badge of cart
    val products: LiveData<List<Product>> = stylishRepository.getProductsInCart()

    // countInCart: Count number for bottom badge
    val countInCart: LiveData<Int> = products.map { it.size }

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    // According to current fragment to change different drawer toggle
    val currentDrawerToggleType: LiveData<DrawerToggleType> = currentFragmentType.map {
        when (it) {
            CurrentFragmentType.PAYMENT -> DrawerToggleType.BACK
            else -> DrawerToggleType.NORMAL
        }
    }

    // Handle navigation to login success
    private val _navigateToLoginSuccess = MutableLiveData<User>()

    val navigateToLoginSuccess: LiveData<User>
        get() = _navigateToLoginSuccess

    // Handle navigation to profile by bottom nav directly which includes icon change
    private val _navigateToProfileByBottomNav = MutableLiveData<User>()

    val navigateToProfileByBottomNav: LiveData<User>
        get() = _navigateToProfileByBottomNav

    // Handle navigation to home by bottom nav directly which includes icon change
    private val _navigateToHomeByBottomNav = MutableLiveData<Boolean>()

    val navigateToHomeByBottomNav: LiveData<Boolean>
        get() = _navigateToHomeByBottomNav

    // it for set up the circle image of an avatar
    val outlineProvider = ProfileAvatarOutlineProvider()

    // check user login status
    val isLoggedIn
        get() = UserManager.isLoggedIn

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

    fun setupUser(user: User) {

        _user.value = user
        Logger.i("=============")
        Logger.i("| setupUser |")
        Logger.i("user=$user")
        Logger.i("MainViewModel=$this")
        Logger.i("=============")
    }

    fun checkUser() {
        if (user.value == null) {
            UserManager.userToken?.let {
                getUserProfile(it)
            }
        }
    }

    fun navigateToLoginSuccess(user: User) {
        _navigateToLoginSuccess.value = user
    }

    fun onLoginSuccessNavigated() {
        _navigateToLoginSuccess.value = null
    }

    fun navigateToProfileByBottomNav(user: User) {
        _navigateToProfileByBottomNav.value = user
    }

    fun onProfileNavigated() {
        _navigateToProfileByBottomNav.value = null
    }

    fun navigateToHomeByBottomNav() {
        _navigateToHomeByBottomNav.value = true
    }

    fun onHomeNavigated() {
        _navigateToHomeByBottomNav.value = null
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
}
