package app.appworks.school.stylish.payment

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDataClass(
    @Json(name = "user_Id")
    val userID: String,
    @Json(name = "checkout_date")
    val checkoutDate: String,
    @Json(name = "order_number")
    val orderNumber: String,
    @Json(name = "total_price")
    val totalPrice:Int,
    @Json(name = "checkout_item")
    val checkoutItem:List<String>,
    val comment:String
) :Parcelable