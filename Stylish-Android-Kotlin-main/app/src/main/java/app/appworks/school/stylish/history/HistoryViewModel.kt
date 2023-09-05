package app.appworks.school.stylish.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.appworks.school.stylish.network.DataStylishApi
import app.appworks.school.stylish.payment.OrderDataClass
import app.appworks.school.stylish.util.ABtest.userId
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private var _orderHistory = MutableLiveData<List<History>>()
    val orderHistory: LiveData<List<History>>
        get() = _orderHistory

    init {
        getOrderHistory()
    }


    private fun getOrderHistory() {
        viewModelScope.launch {
            try {
                val dataList = DataStylishApi.retrofitService.getOrderHistory(userId)
                _orderHistory.value = dataList
                Log.i("HISTORY", "${dataList}")
                Log.i("HISTORY2", "${_orderHistory.value}")

            } catch (e: Exception) {
                Log.i("HISTORY FAILED", "${e.message}")
            }
        }
    }
}