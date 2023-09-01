package app.appworks.school.stylish.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.util.Logger

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [CheckoutSuccessFragment].
 */
class CheckoutSuccessViewModel(private val stylishRepository: StylishRepository) : ViewModel() {

    // Handle navigation to home
    private val _navigateToHome = MutableLiveData<Boolean>()

    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]$this")
        Logger.i("------------------------------------")
    }

    fun navigateToHome() {
        _navigateToHome.value = true
    }

    fun onHomeNavigated() {
        _navigateToHome.value = null
    }
}
