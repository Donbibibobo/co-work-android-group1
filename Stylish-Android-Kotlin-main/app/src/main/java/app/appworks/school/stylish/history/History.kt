package app.appworks.school.stylish.history

import java.time.LocalDateTime

data class History(
    val date:LocalDateTime,
    val orderNumber:String,
    val amount:Int,
    val remark:String
)