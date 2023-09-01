package app.appworks.school.stylish.network

import app.appworks.school.stylish.data.NativeSignInBody
import app.appworks.school.stylish.data.NativeSignUpBody
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Test
import java.io.IOException

class StylishUerApiTest : StylishApiServiceTest() {

    private val rawResponse =
        """
        {
          "data": {
            "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6joiYXJ0aHVIjoxNjEzNTY3MzA0fQ.6EPCOfBGynidAfpVqlvbHGWHCJ5LZLtKvPaQ",
            "access_expired": 3600,
            "user": {
              "id": 11245642,
              "provider": "facebook",
              "name": "Pei",
              "email": "pei@appworks.tw",
              "picture": "https://schoolvoyage.ga/images/123498.png"
            }
          }
        }
        """.trimIndent()

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun test_user_profile_get() {
        val mockResponse = MockResponse()
        mockWebServer.enqueue(
            mockResponse.setBody(
                """
                {
                  "data": {
                    "provider": "facebook",
                    "name": "Pei",
                    "email": "pei@appworks.tw",
                    "picture": "https://schoolvoyage.ga/images/123498.png"
                  }
                }
                """.trimIndent()
            )
        )
        runBlocking {
            val userResult = service.getUserProfile("TOKEN")
            Assert.assertTrue(userResult.error == null)
            Assert.assertTrue(userResult.user != null)
            Assert.assertEquals("pei@appworks.tw", userResult.user!!.email)
            Assert.assertEquals("facebook", userResult.user!!.provider)
            Assert.assertEquals("Pei", userResult.user!!.name)
            Assert.assertEquals(
                "https://schoolvoyage.ga/images/123498.png",
                userResult.user!!.picture
            )
        }
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun test_user_sign_in_post() {
        val mockRequestBody = NativeSignInBody("provider", "email", "pwd")
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(rawResponse))
        runBlocking {
            val signInResult = service.userSignIn(mockRequestBody)
            Assert.assertTrue(signInResult.error == null)
            Assert.assertTrue(signInResult.userSignIn != null)
            Assert.assertEquals(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6joiYXJ0aHVIjoxNjEzNTY3MzA0fQ.6EPCOfBGynidAfpVqlvbHGWHCJ5LZLtKvPaQ",
                signInResult.userSignIn!!.accessToken
            )
            Assert.assertEquals(3600, signInResult.userSignIn!!.accessExpired)
            Assert.assertEquals(
                11245642,
                signInResult.userSignIn!!.user.id
            ) //TODO: should be using long
            Assert.assertEquals("facebook", signInResult.userSignIn!!.user.provider)
            Assert.assertEquals("Pei", signInResult.userSignIn!!.user.name)
            Assert.assertEquals("pei@appworks.tw", signInResult.userSignIn!!.user.email)
            Assert.assertEquals(
                "https://schoolvoyage.ga/images/123498.png",
                signInResult.userSignIn!!.user.picture
            )
        }
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun test_user_sign_up_post() {
        val mockRequestBody = NativeSignUpBody("provider", "email", "pwd")
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(rawResponse))
        runBlocking {
            val signInResult = service.userSignUp(mockRequestBody)
            Assert.assertTrue(signInResult.error == null)
            Assert.assertTrue(signInResult.userSignIn != null)
            Assert.assertEquals(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6joiYXJ0aHVIjoxNjEzNTY3MzA0fQ.6EPCOfBGynidAfpVqlvbHGWHCJ5LZLtKvPaQ",
                signInResult.userSignIn!!.accessToken
            )
            Assert.assertEquals(3600, signInResult.userSignIn!!.accessExpired)
            Assert.assertEquals(
                11245642,
                signInResult.userSignIn!!.user.id
            ) //TODO: should be using long
            Assert.assertEquals("facebook", signInResult.userSignIn!!.user.provider)
            Assert.assertEquals("Pei", signInResult.userSignIn!!.user.name)
            Assert.assertEquals("pei@appworks.tw", signInResult.userSignIn!!.user.email)
            Assert.assertEquals(
                "https://schoolvoyage.ga/images/123498.png",
                signInResult.userSignIn!!.user.picture
            )
        }
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun test_native_sign_in_body_parsing() {
        val body = moshi.adapter(NativeSignInBody::class.java).fromJson(
            """
            {
              "provider":"native",
              "email":"test@test.com",
              "password":"test"
            }
            """.trimIndent()
        )
        Assert.assertTrue(body != null)
        Assert.assertEquals("native", body!!.provider)
        Assert.assertEquals("test@test.com", body.email)
        Assert.assertEquals("test", body.password)
    }
}