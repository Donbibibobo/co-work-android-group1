package app.appworks.school.stylish.network

import androidx.annotation.CallSuper
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

abstract class StylishApiServiceTest {
    protected lateinit var mockWebServer: MockWebServer
    protected lateinit var service: StylishApiService

    @Before
    @CallSuper
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(StylishApiService::class.java)
    }

    @After
    @CallSuper
    fun stopService() {
        mockWebServer.shutdown()
    }
}