package app.appworks.school.stylish.chat

import app.appworks.school.stylish.data.HomeItem
import app.appworks.school.stylish.data.Product

sealed class ChatDataClass {
    data class Sent(val product: String) : ChatDataClass()
    data class Received(val product: String) : ChatDataClass()
    data class Img(val product: String) : ChatDataClass()

}