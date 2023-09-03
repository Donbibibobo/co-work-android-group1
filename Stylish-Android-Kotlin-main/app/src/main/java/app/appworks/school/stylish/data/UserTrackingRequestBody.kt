package app.appworks.school.stylish.data

data class UserTrackingRequestBody (
    val userID: String,
    val event_type: String,
    val event_detail: String,
    val timestamp: String,
    val version: String
)