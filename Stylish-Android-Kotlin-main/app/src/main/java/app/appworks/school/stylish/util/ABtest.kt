package app.appworks.school.stylish.util

import app.appworks.school.stylish.data.Product
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
}