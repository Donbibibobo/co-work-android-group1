package app.appworks.school.stylish.network

import app.appworks.school.stylish.BuildConfig
import app.appworks.school.stylish.data.*
import app.appworks.school.stylish.history.History
import app.appworks.school.stylish.payment.OrderDataClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

/**
 * Created by Wayne Chen in Jul. 2019.
 */

// school api
private const val HOST_NAME = "api.appworks-school.tw"
private const val API_VERSION = "1.0"
private const val BASE_URL = "https://$HOST_NAME/api/$API_VERSION/"

// data api
private const val DATA_HOST_NAME = "54.66.20.75:8080"
private const val DATA_API_VERSION = "1.0"
private const val DATA_BASE_URL = "http://$DATA_HOST_NAME/api/$DATA_API_VERSION/"

// user tracking api & chat box
private const val USER_HOST_NAME = "54.66.20.75:8080"
private const val USER_API_VERSION = "1.0"
private const val USER_BASE_URL = "http://$USER_HOST_NAME/api/$USER_API_VERSION/"

// data api
private const val REVIEW_HOST_NAME = "54.66.20.75:8080"
private const val REVIEW_API_VERSION = "1.0"
private const val REVIEW_BASE_URL = "http://$REVIEW_HOST_NAME/api/$REVIEW_API_VERSION/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
// normal moshi
internal val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .add(
        PolymorphicJsonAdapterFactory.of(IUserTracking::class.java, "eventType")
            .withSubtype(UserTrackingRequestBodyString::class.java,"login")
            .withSubtype(UserTrackingRequestBodyString::class.java,"viewItem")
            .withSubtype(UserTrackingRequestBodyString::class.java,"addToCart")
            .withSubtype(UserTrackingRequestBodyCollect::class.java,"collect")
            .withSubtype(UserTrackingRequestBodyCheckout::class.java,"checkout")
    )
    .build()

private val client = OkHttpClient.Builder()
    .addInterceptor(
        HttpLoggingInterceptor().apply {
            level = when (BuildConfig.LOGGER_VISIABLE) {
                true -> HttpLoggingInterceptor.Level.HEADERS
                false -> HttpLoggingInterceptor.Level.NONE
            }
        }
    ).addInterceptor(
        Interceptor { chain ->
            val request = chain.request()
            chain.proceed(request.newBuilder().header("Connection", "close").build())
        }
    )
    .build()

val adapterWishList = moshi.adapter(ProductList::class.java)

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */

// school api
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

// data api
private val dataRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(DATA_BASE_URL)
    .client(client)
    .build()


// user tracking api & chat box
private val userRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(USER_BASE_URL)
    .client(client)
    .build()

// review api
private val reviewRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(REVIEW_BASE_URL)
    .client(client)
    .build()

/**
 * A public interface that exposes the [getMarketingHots], [getProductList], [getUserProfile],
 * [userSignIn], [checkoutOrder] methods
 */
interface StylishApiService {
    /**
     * Returns a Coroutine [Deferred] [MarketingHotsResult] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "marketing/hots" endpoint will be requested with the GET HTTP method
     */
    @GET("marketing/hots")
    suspend fun getMarketingHots(): MarketingHotsResult

    /**
     * Returns a Coroutine [Deferred] [ProductListResult] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "products/{catalogType}" endpoint will be requested with the GET
     * HTTP method (catalogType: men, women, accessories)
     * The @Query annotation indicates that it will be added "?paging={pagingKey}" after endpoint
     */
    @GET("products/{catalogType}")
    suspend fun getProductList(
        @Path("catalogType") type: String,
        @Query("paging") paging: String? = null
    ): ProductListResult

    /**
     * Returns a Coroutine [Deferred] [UserProfileResult] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "user/profile" endpoint will be requested with the GET HTTP method
     * The @Header annotation indicates that it will be added "Authorization" header
     */
    @GET("user/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String): UserProfileResult

    //get detail review
    @GET("products/details")
    suspend fun getDetailReview(
        @Query("id") productId: Long
    ): ReviewSubmit

    //get order history from server
    @GET("user/order")
    suspend fun getOrderHistory(
        @Query("userID") userId: String
    ): List<History>

    /**
     * Returns a Coroutine [Deferred] [UserSignInResult] which can be fetched with await() if in a Coroutine scope.
     * The @POST annotation indicates that the "user/signin" endpoint will be requested with the POST HTTP method
     * The @Field annotation indicates that it will be added "provider", "access_token" key-pairs to the body of
     * the POST HTTP method, and it have to use @FormUrlEncoded to support @Field
     */

    @FormUrlEncoded
    @POST("user/signin")
    suspend fun userSignIn(
        @Field("provider") provider: String = "facebook",
        @Field("access_token") fbToken: String
    ): UserSignInResult

    @POST("user/signin")
    suspend fun userSignIn(
        @Body nativeSignInBody: NativeSignInBody
    ): UserSignInResult

    @POST("user/signup")
    suspend fun userSignUp(
        @Body nativeSignUpBody: NativeSignUpBody
    ): UserSignUpResult

    /**
     * Returns a Coroutine [Deferred] [CheckoutOrderResult] which can be fetched with await() if in a Coroutine scope.
     * The @POST annotation indicates that the "user/signin" endpoint will be requested with the POST HTTP method
     * The @Header annotation indicates that it will be added "Authorization" header
     * The @Body annotation indicates that it will be added [OrderDetail] to the body of the POST HTTP method
     */
    @POST("order/checkout")
    suspend fun checkoutOrder(
        @Header("Authorization") token: String,
        @Body orderDetail: OrderDetail
    ): CheckoutOrderResult




    @Headers("Content-Type: application/json")
    @POST("review/submit")
    suspend fun reviewSubmit(
        @Body request: ReviewSubmitRequestBody,
    )


    @Headers("Content-type: application/json")
    @POST("user/tracking")
    suspend fun userTrackingPoly(
        @Body request: IUserTracking,
    ): UserTracking


    // chat box
    @Headers("Content-type: application/json")
    @POST("recommendation/chatbox")
    suspend fun userTrackingChat(
        @Body request: ChatBoxAPI,
    ): ChatBoxBack

    // chat box image
    @Multipart
    @POST("recommendation/smart_image")
    suspend fun userTrackingChatImage(
        @Part image: MultipartBody.Part,
    ): ChatBoxBack

    // order
    @Headers("Content-type: application/json")
    @POST("user/order")
    suspend fun insertOrderHistory(
        @Body request: OrderDataClass
    )

}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
// school
object StylishApi {
    val retrofitService: StylishApiService by lazy { retrofit.create(StylishApiService::class.java) }
}

// data
object DataStylishApi {
    val retrofitService: StylishApiService by lazy { dataRetrofit.create(StylishApiService::class.java) }
}

// user tracking & chat box
object UserStylishApi {
    val retrofitService: StylishApiService by lazy { userRetrofit.create(StylishApiService::class.java) }
}

// review submit
object ReviewStylishApi {
    val retrofitService: StylishApiService by lazy { reviewRetrofit.create(StylishApiService::class.java) }
}
