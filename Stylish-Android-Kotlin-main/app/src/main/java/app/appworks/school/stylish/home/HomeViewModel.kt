package app.appworks.school.stylish.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.stylish.R
import app.appworks.school.stylish.data.HomeItem
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.Result
import app.appworks.school.stylish.data.source.StylishRepository
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
 * The [ViewModel] that is attached to the [HomeFragment].
 */
class HomeViewModel(private val stylishRepository: StylishRepository) : ViewModel() {

    private val _homeItems = MutableLiveData<List<HomeItem>>()

    val homeItems: LiveData<List<HomeItem>>
        get() = _homeItems

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Product>()

    val navigateToDetail: LiveData<Product>
        get() = _navigateToDetail

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

    /**
     * Call getMarketingHotsResult() on init so we can display status immediately.
     */
    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]$this")
        Logger.i("------------------------------------")

        getMarketingHotsResult(true)
    }

    /**
     * track [StylishRepository.getMarketingHots]: -> [DefaultStylishRepository] : [StylishRepository] -> [StylishRemoteDataSource] : [StylishDataSource]
     */
    private fun getMarketingHotsResult(isInitial: Boolean = false) {

        coroutineScope.launch {

            if (isInitial) _status.value = LoadApiStatus.LOADING

            val result = stylishRepository.getMarketingHots()

            _homeItems.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    if (isInitial) _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    if (isInitial) _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    if (isInitial) _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = getString(R.string.you_know_nothing)
                    if (isInitial) _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }

    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            getMarketingHotsResult()
        }
    }

    fun navigateToDetail(product: Product) {
        _navigateToDetail.value = product
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}
