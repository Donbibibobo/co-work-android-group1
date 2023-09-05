package app.appworks.school.stylish.history

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

//@Parcelize
//data class HistoryDataList(
//    val historyList: List<History>
//) : Parcelable

@Parcelize
data class History(
    @Json(name = "user_Id")
    val userID: String,
    @Json(name = "checkout_date")
    val checkoutDate: String,
    @Json(name = "order_number")
    val orderNumber: String,
    @Json(name = "total_price")
    val totalPrice: Int,
//    @Json(name = "checkout_item")
//    val checkoutItem: List<String>,
    val comment: String
):Parcelable