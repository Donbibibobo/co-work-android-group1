package app.appworks.school.stylish.network

import app.appworks.school.stylish.BuildConfig
import app.appworks.school.stylish.data.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
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

// user tracking api
private const val USER_HOST_NAME = "54.66.20.75"
private const val USER_API_VERSION = "1.0"
private const val USER_BASE_URL = "https://$USER_HOST_NAME/api/$USER_API_VERSION/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
internal val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val client = OkHttpClient.Builder()
    .addInterceptor(
        HttpLoggingInterceptor().apply {
            level = when (BuildConfig.LOGGER_VISIABLE) {
                true -> HttpLoggingInterceptor.Level.BODY
                false -> HttpLoggingInterceptor.Level.NONE
            }
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


// user tracking api
private val userRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(USER_BASE_URL)
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





    // user tracking api
    @POST("user/tracking")
    @FormUrlEncoded
    suspend fun userTracking(
        @Field("userID") userId: String,
        @Field("event_type") eventType: String,
        @Field("event_detail") eventDetail: String,
        @Field("timestamp") timestamp: String,
        @Field("version") version: String
    )

//    @Headers("Content-type: application/json")
//    @POST("user/tracking")
//    suspend fun userTracking2(
//        @Body request: UserTrackingRequestBody,
//    ): Unit




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

// user tracking
object UserStylishApi {
    val retrofitService: StylishApiService by lazy { userRetrofit.create(StylishApiService::class.java) }
}
