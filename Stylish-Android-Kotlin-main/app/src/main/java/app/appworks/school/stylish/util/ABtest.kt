package app.appworks.school.stylish.util

import android.util.Log
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.UserTrackingRequestBody
import app.appworks.school.stylish.network.UserStylishApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

    // for user tracking api [view_item]
    fun userTrackingApiViewItemScope(productId: Long){
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)
        scope.launch {
//            val request = UserTrackingRequestBody(userId, "view_item", "$productId", getCurrentDateTime(), version)
//            val response = UserStylishApi.retrofitService.userTracking(request)
//            Log.i("userTracking", "[view_item]: ${response.message}")
//            Log.i("userTracking", "[view_item_content]: $request")
        }
    }
}