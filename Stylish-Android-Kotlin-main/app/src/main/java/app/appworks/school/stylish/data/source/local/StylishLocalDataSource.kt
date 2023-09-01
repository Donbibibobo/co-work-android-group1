package app.appworks.school.stylish.data.source.local

import androidx.lifecycle.LiveData
import app.appworks.school.stylish.data.*
import app.appworks.school.stylish.data.source.StylishDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Concrete implementation of a Stylish source as a db.
 */
class StylishLocalDataSource(private val dao: StylishDatabaseDao) : StylishDataSource {

    override suspend fun getMarketingHots(): Result<List<HomeItem>> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProductList(type: String, paging: String?): Result<ProductListResult> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUserProfile(token: String): Result<User> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun userSignIn(fbToken: String): Result<UserSignInResult> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun userSignIn(email: String, password: String): Result<UserSignInResult> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun userSignUp(
        name: String,
        email: String,
        password: String
    ): Result<UserSignUpResult> {
        TODO("Not yet implemented")
    }

    override suspend fun checkoutOrder(
        token: String,
        orderDetail: OrderDetail
    ): Result<CheckoutOrderResult> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getProductsInCart(): LiveData<List<Product>> {
        return dao.getAllProducts()
    }

    override suspend fun isProductInCart(id: Long, colorCode: String, size: String): Boolean {
        return withContext(Dispatchers.IO) {
            dao.get(id, colorCode, size) != null
        }
    }

    override suspend fun insertProductInCart(product: Product) {
        withContext(Dispatchers.IO) {
            dao.insert(product)
        }
    }

    override suspend fun updateProductInCart(product: Product) {
        withContext(Dispatchers.IO) {
            dao.update(product)
        }
    }

    override suspend fun removeProductInCart(id: Long, colorCode: String, size: String) {
        withContext(Dispatchers.IO) {
            dao.delete(id, colorCode, size)
        }
    }

    override suspend fun clearProductInCart() {
        withContext(Dispatchers.IO) {
            dao.clear()
        }
    }
}
