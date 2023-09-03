package app.appworks.school.stylish.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object ABtest {

    var version = ""

    var userId = ""


    fun getCurrentDateTime(): String {
        val utcInstant = Instant.now()
        val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val zoneId = ZoneId.of("UTC")
        val dateTime = LocalDateTime.ofInstant(utcInstant, zoneId)
        return dateTime.format(outputFormat)
    }
}