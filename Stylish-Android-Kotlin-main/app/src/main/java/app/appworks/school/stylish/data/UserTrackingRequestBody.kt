package app.appworks.school.stylish.data

import com.squareup.moshi.Json

// PolymorphicJsonAdapterFactory ========================================

interface IUserTracking

data class UserTrackingRequestBodyString (
    val userID: String,
    @Json(name = "event_type") val eventType: String,
    @Json(name = "event_detail") val eventDetail: String,
    val timestamp: String,
    val version: String
): IUserTracking

data class UserTrackingRequestBodyCheckout (
    val userID: String,
    @Json(name = "event_type") val eventType: String,
    @Json(name = "event_detail") val eventDetail: UserTrackingCheckout,
    val timestamp: String,
    val version: String
): IUserTracking

data class UserTrackingRequestBodyCollect (
    val userID: String,
    @Json(name = "event_type") val eventType: String,
    @Json(name = "event_detail") val eventDetail: UserTrackingCollect,
    val timestamp: String,
    val version: String
): IUserTracking



// event_detail: Checkout
data class UserTrackingCheckout (
    @Json(name = "checkout_item") val checkoutItem: List<String>,
)

// event_detail: Collect
data class UserTrackingCollect (
    val action: String,
    @Json(name = "collect_item") val collectItem: String,
)


// chat box ========================================

data class ChatBoxAPI (
    val tag: String
)

// chat box Image ========================================

data class ChatBoxImageAPI (
    val image: String
)



