package app.appworks.school.stylish.util

import android.util.Log
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.UserTrackingCheckout
import app.appworks.school.stylish.data.UserTrackingRequestBodyCheckout
import app.appworks.school.stylish.data.UserTrackingRequestBodyString
import app.appworks.school.stylish.network.UserStylishApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.TimerTask

object ABtest {
    var wishlist = mutableListOf<Product>()

    var version = ""

    var userId = ""


    fun getCurrentDateTime(): String {
        val utcInstant = Instant.now()
        val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val zoneId = ZoneId.of("UTC")
        val dateTime = LocalDateTime.ofInstant(utcInstant, zoneId)
        return dateTime.format(outputFormat)
    }

    // for user tracking api [view_item] ====================================
    fun userTrackingApiViewItemScope(productId: Long){
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)
        scope.launch {
            val request = UserTrackingRequestBodyString(userId, "view_item", "$productId", getCurrentDateTime(), version)
            val response = UserStylishApi.retrofitService.userTrackingPoly(request)
            Log.i("userTracking", "[view_item]: ${response.message}")
            Log.i("userTracking", "[view_item_content]: $request")
        }
    }

    // for user tracking api [checkout] ====================================
    fun userTrackingApiCheckoutScope(products: List<Product>){
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)
        scope.launch {

            val productIdList = mutableListOf<String>()

            products.forEach {
                productIdList.add(it.id.toString())
            }

            val eventDetail = UserTrackingCheckout(productIdList)

            val request = UserTrackingRequestBodyCheckout(userId, "checkout", eventDetail, getCurrentDateTime(), version)
            val response = UserStylishApi.retrofitService.userTrackingPoly(request)
            Log.i("userTracking", "[checkout]: ${response.message}")
            Log.i("userTracking", "[checkout_content]: $request")
        }
    }


    // timer
    private var timer: java.util.Timer? = null
    var seconds = 0

    fun startTimer() {
        timer = java.util.Timer()

        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                seconds++
                Log.i("ServiceTest","Seconds: $seconds")
            }
        }, 0, 1000)
    }

    fun stopTimer(): Int {
        timer?.cancel()
        timer?.purge()
        timer = null
        return seconds
    }



}