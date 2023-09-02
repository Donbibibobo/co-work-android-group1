package app.appworks.school.stylish.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ABtest {

    var version = ""

    var userId = ""

    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now().toString()

        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val dateTime = LocalDateTime.parse(currentDateTime, inputFormat)
        return dateTime.format(outputFormat)
    }
}