package app.appworks.school.stylish.data

data class ReviewSubmitRequestBody(
    val userID: String,
    val product_id: Long,
    val review: String,
    val timestamp: String,
    val version: String
)
