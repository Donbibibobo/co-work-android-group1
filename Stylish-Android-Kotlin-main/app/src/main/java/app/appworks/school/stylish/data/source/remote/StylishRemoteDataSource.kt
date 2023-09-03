package app.appworks.school.stylish.data.source.remote

import androidx.lifecycle.LiveData
import app.appworks.school.stylish.R
import app.appworks.school.stylish.data.*
import app.appworks.school.stylish.data.source.StylishDataSource
import app.appworks.school.stylish.network.DataStylishApi
import app.appworks.school.stylish.network.StylishApi
import app.appworks.school.stylish.util.Logger
import app.appworks.school.stylish.util.Util.getString
import app.appworks.school.stylish.util.Util.isInternetConnected

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Implementation of the Stylish source that from network.
 */
object StylishRemoteDataSource : StylishDataSource {

    override suspend fun getMarketingHots(): Result<List<HomeItem>> {

        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        return try {
            // this will run on a thread managed by Retrofit
            val listResult = DataStylishApi.retrofitService.getMarketingHots()

            listResult.error?.let {
                return Result.Fail(it)
            }
            Result.Success(listResult.toHomeItems())
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getProductList(type: String, paging: String?): Result<ProductListResult> {

        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        return try {
            // this will run on a thread managed by Retrofit
            val listResult = DataStylishApi.retrofitService.getProductList(type = type, paging = paging)

            listResult.error?.let {
                return Result.Fail(it)
            }
            Result.Success(listResult)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getUserProfile(token: String): Result<User> {

        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        return try {
            // this will run on a thread managed by Retrofit
            val listResult = StylishApi.retrofitService.getUserProfile(token)

            listResult.error?.let {
                return Result.Fail(it)
            }
            listResult.user?.let {
                return Result.Success(it)
            }
            Result.Fail(getString(R.string.you_know_nothing))
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun userSignIn(fbToken: String): Result<UserSignInResult> {

        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        return try {
            // this will run on a thread managed by Retrofit
            val listResult = StylishApi.retrofitService.userSignIn(fbToken = fbToken)

            listResult.error?.let {
                return Result.Fail(it)
            }
            Result.Success(listResult)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun userSignIn(email: String, password: String): Result<UserSignInResult> {

        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        return try {
            // this will run on a thread managed by Retrofit
            val listResult = StylishApi.retrofitService.userSignIn(
                NativeSignInBody(
                    email = email,
                    password = password
                )
            )

            listResult.error?.let {
                return Result.Fail(it)
            }
            Result.Success(listResult)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun userSignUp(name: String, email: String, password: String): Result<UserSignUpResult> {

        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        return try {
            // this will run on a thread managed by Retrofit
            val listResult = StylishApi.retrofitService.userSignUp(
                NativeSignUpBody(
                    name = name,
                    email = email,
                    password = password
                )
            )

            listResult.error?.let {
                return Result.Fail(it)
            }
            Result.Success(listResult)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun checkoutOrder(
        token: String,
        orderDetail: OrderDetail
    ): Result<CheckoutOrderResult> {

        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        return try {
            // this will run on a thread managed by Retrofit
            val listResult = StylishApi.retrofitService.checkoutOrder(token, orderDetail)

            listResult.error?.let {
                return Result.Fail(it)
            }
            Result.Success(listResult)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override fun getProductsInCart(): LiveData<List<Product>> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun isProductInCart(id: Long, colorCode: String, size: String): Boolean {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insertProductInCart(product: Product) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateProductInCart(product: Product) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun removeProductInCart(id: Long, colorCode: String, size: String) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun clearProductInCart() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
