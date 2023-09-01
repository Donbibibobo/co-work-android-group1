package app.appworks.school.stylish.network

import app.appworks.school.stylish.data.OrderDetail
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class StylishCheckoutTest : StylishApiServiceTest() {

    private val rawOrderDetail = """
            {
              "prime": "ccc1491581661f700bcc1cafec673c741f0665ca77550fe828ef38ee1437a2b8",
              "order": {
                "shipping": "delivery",
                "payment": "credit_card",
                "subtotal": 1234,
                "freight": 14,
                "total": 1300,
                "recipient": {
                  "name": "Luke",
                  "phone": "0987654321",
                  "email": "luke@gmail.com",
                  "address": "市政府站",
                  "time": "morning"
                },
                "list": [
                  {
                    "id": "201807202157",
                    "name": "活力花紋長筒牛仔褲",
                    "price": 1299,
                    "color": {
                        "code": "DDF0FF",
                        "name": "淺藍"
                    },
                    "size": "M",
                    "qty": 1
                  }
                ]
              }
            }
        """.trimIndent()

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun test_checkout_post() {
        val mockResponse = MockResponse()
        mockWebServer.enqueue(
            mockResponse.setBody(
                """
                    {
                      "data": {
                        "number":"4465123465"
                      }
                    }
                """.trimIndent()
            )
        )
        val detail = moshi.adapter(OrderDetail::class.java).fromJson(rawOrderDetail)
        runBlocking {
            assertTrue(detail != null)
            val checkoutResult = service.checkoutOrder("TOKEN", detail!!)
            assertTrue(checkoutResult.data != null)
            assertEquals("4465123465", checkoutResult.data!!.number)
        }
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun test_order_detail_parsing() {
        val detail = moshi.adapter(OrderDetail::class.java).fromJson(rawOrderDetail)
        assertTrue(detail != null)
        assertEquals(
            "ccc1491581661f700bcc1cafec673c741f0665ca77550fe828ef38ee1437a2b8",
            detail!!.prime
        )
        assertEquals("delivery", detail.order.shipping)
        assertEquals("credit_card", detail.order.payment)
        assertEquals(1234, detail.order.subtotal)
        assertEquals(14, detail.order.freight)
        assertEquals(1300, detail.order.total)
        assertEquals("Luke", detail.order.recipient.name)
        assertEquals("0987654321", detail.order.recipient.phone)
        assertEquals("luke@gmail.com", detail.order.recipient.email)
        assertEquals("市政府站", detail.order.recipient.address)
        assertEquals("morning", detail.order.recipient.time)
        assertEquals(1, detail.order.list.size)
        assertEquals(201807202157, detail.order.list.first().id) //TODO: should be String
        assertEquals("活力花紋長筒牛仔褲", detail.order.list.first().name)
        assertEquals(1299, detail.order.list.first().price)
        assertEquals("DDF0FF", detail.order.list.first().color.code)
        assertEquals("淺藍", detail.order.list.first().color.name)
        assertEquals("M", detail.order.list.first().size)
        assertEquals(1, detail.order.list.first().qty)
    }
}