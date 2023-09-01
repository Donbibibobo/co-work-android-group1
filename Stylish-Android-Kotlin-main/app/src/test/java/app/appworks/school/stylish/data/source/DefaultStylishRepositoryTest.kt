package app.appworks.school.stylish.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.appworks.school.stylish.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DefaultStylishRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var localDataSource: StylishDataSource

    @Mock
    private lateinit var remoteDateSource: StylishDataSource

    private lateinit var stylishRepo: DefaultStylishRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        stylishRepo = DefaultStylishRepository(
            stylishRemoteDataSource = remoteDateSource,
            stylishLocalDataSource = localDataSource
        )

    }

    @Test
    fun test_get_market_hots_call_to_correct_instance() {
        runBlocking {
            stylishRepo.getMarketingHots()
            verify(localDataSource, times(0)).getMarketingHots()
            verify(remoteDateSource, times(1)).getMarketingHots()
        }
    }

    @Test
    fun test_get_product_list_call_to_correct_instance() {
        runBlocking {
            stylishRepo.getProductList("", "")
            verify(localDataSource, times(0)).getProductList("", "")
            verify(remoteDateSource, times(1)).getProductList("", "")
        }
    }

    @Test
    fun test_get_user_profile_call_to_correct_instance() {
        runBlocking {
            stylishRepo.getUserProfile("")
            verify(localDataSource, times(0)).getUserProfile("")
            verify(remoteDateSource, times(1)).getUserProfile("")
        }
    }

    @Test
    fun test_user_sign_in_called_to_correct_instance() {
        runBlocking {
            stylishRepo.userSignIn("")
            verify(localDataSource, times(0)).userSignIn("")
            verify(remoteDateSource, times(1)).userSignIn("")
        }
    }

    @Test
    fun test_user_sign_in_called_to_correct_instance2() {
        runBlocking {
            stylishRepo.userSignIn("", "")
            verify(localDataSource, times(0)).userSignIn("", "")
            verify(remoteDateSource, times(1)).userSignIn("", "")
        }
    }

    @Test
    fun test_user_signup_called_to_correct_instance() {
        runBlocking {
            stylishRepo.userSignUp("", "", "")
            verify(localDataSource, times(0)).userSignUp("", "", "")
            verify(remoteDateSource, times(1)).userSignUp("", "", "")
        }
    }

    @Test
    fun test_check_out_order_called_to_correct_instance() {
        val orderDetail = OrderDetail(
            "",
            Order(
                "", "", 1L, 1L, 1L, Recipient(
                    "", "", "", "", ""
                ), listOf()
            )
        )
        runBlocking {
            stylishRepo.checkoutOrder("", orderDetail)
            verify(localDataSource, times(0)).checkoutOrder("", orderDetail)
            verify(remoteDateSource, times(1)).checkoutOrder("", orderDetail)
        }
    }

    @Test
    fun test_get_product_in_cart_called_to_correct_instance() {
        runBlocking {
            stylishRepo.getProductsInCart()
            verify(localDataSource, times(1)).getProductsInCart()
            verify(remoteDateSource, times(0)).getProductsInCart()
        }
    }

    @Test
    fun test_get_is_product_in_cart_called_to_correct_instance() {
        runBlocking {
            stylishRepo.isProductInCart(0L, "", "")
            verify(localDataSource, times(1)).isProductInCart(0L, "", "")
            verify(remoteDateSource, times(0)).isProductInCart(0L, "", "")
        }
    }

    @Test
    fun test_insert_product_in_cart_called_to_correct_instance() {
        runBlocking {
            val product = Product(
                0L, "", "", 0, "",
                "", "", "", "",
                listOf(), listOf(), listOf(), "", listOf()
            )
            stylishRepo.insertProductInCart(product)
            verify(localDataSource, times(1)).insertProductInCart(product)
            verify(remoteDateSource, times(0)).insertProductInCart(product)
        }
    }

    @Test
    fun test_update_product_in_cart_called_to_correct_instance() {
        runBlocking {
            val product = Product(
                0L, "", "", 0, "",
                "", "", "", "",
                listOf(), listOf(), listOf(), "", listOf()
            )
            stylishRepo.updateProductInCart(product)
            verify(localDataSource, times(1)).updateProductInCart(product)
            verify(remoteDateSource, times(0)).updateProductInCart(product)
        }
    }

    @Test
    fun test_remove_product_in_cart_called_to_correct_instance() {
        runBlocking {
            stylishRepo.removeProductInCart(0L, "", "")
            verify(localDataSource, times(1)).removeProductInCart(0L, "", "")
            verify(remoteDateSource, times(0)).removeProductInCart(0L, "", "")
        }
    }

    @Test
    fun test_clear_product_in_cart_called_to_correct_instance() {
        runBlocking {
            stylishRepo.clearProductInCart()
            verify(localDataSource, times(1)).clearProductInCart()
            verify(remoteDateSource, times(0)).clearProductInCart()
        }
    }
}