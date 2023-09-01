package app.appworks.school.stylish.payment

import androidx.core.os.trace
import androidx.lifecycle.*
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.data.*
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.ext.toOrderProductList
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.network.LoadApiStatus
import app.appworks.school.stylish.util.ABtest
import app.appworks.school.stylish.util.Logger
import app.appworks.school.stylish.util.Util.getColor
import app.appworks.school.stylish.util.Util.getString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tech.cherri.tpdirect.api.*
import tech.cherri.tpdirect.model.TPDStatus

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [PaymentFragment].
 */
class PaymentViewModel(private val stylishRepository: StylishRepository) : ViewModel() {










    // get products in cart from Room database
    val products: LiveData<List<Product>> = stylishRepository.getProductsInCart()

    val name = MutableLiveData<String>()

    val email = MutableLiveData<String>()

    val phone = MutableLiveData<String>()

    val address = MutableLiveData<String>()

    val selectedTimeRadio = MutableLiveData<Int>()

    private val shippingTime: String
        get() = when (selectedTimeRadio.value) {
            R.id.radio_shipping_morning -> getString(R.string.morning)
            R.id.radio_shipping_afternoon -> getString(R.string.afternoon)
            R.id.radio_shipping_anytime -> getString(R.string.anytime)
            else -> ""
        }

    val selectedPaymentMethodPosition = MutableLiveData<Int>()

    val paymentMethod: LiveData<PaymentMethod> = selectedPaymentMethodPosition.map {
        PaymentMethod.values()[it]
    }

    val totalPrice: LiveData<Long> = products.map {
        var totalPrice = 0L
        products.value?.let {
            for (product in it) {
                product.amount?.let { amount ->
                    totalPrice += (product.price.toLong() * amount)
                }
            }
        }
        totalPrice
    }

    val totalFreight: LiveData<Long> = products.map {
        var totalPrice = 0L
        products.value?.let {
            for (product in it) {
                product.amount?.let { amount ->
                    totalPrice += (product.price.toLong() * amount)
                }
            }
        }
        (totalPrice * 0.0087).toLong()
    }

    val totalOrderPrice = MediatorLiveData<Long>().apply {
        addSource(totalPrice) {
            it?.let { value = it + (totalFreight.value ?: 0) }
        }
        addSource(totalFreight) {
            it?.let { value = it + (totalPrice.value ?: 0) }
        }
    }

    // Handle the error for checkout
    private val _invalidCheckout = MutableLiveData<Int>()

    val invalidCheckout: LiveData<Int>
        get() = _invalidCheckout

    var tpdCard: TPDCard? = null

    private var isCanGetPrime: Boolean = false

    var tpdErrorMessage: String = ""

    // Handle when checkout is successful
    private val _checkoutSuccess = MutableLiveData<CheckoutOrderResult>()

    val checkoutSuccess: LiveData<CheckoutOrderResult>
        get() = _checkoutSuccess

    // Handle navigation to checkout success page
    private val _navigateToCheckoutSuccess = MutableLiveData<Boolean>()

    val navigateToCheckoutSuccess: LiveData<Boolean>
        get() = _navigateToCheckoutSuccess

    // Handle navigation to login dialog
    private val _navigateToLogin = MutableLiveData<Boolean>()

    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]$this")
        Logger.i("------------------------------------")
    }

    /**
     * Prepare to checkout, make sure the input user info are not empty first, and then check the [TPDStatus] of [TPDForm]
     */
    fun prepareCheckout() {

        when {
            name.value.isNullOrEmpty() -> _invalidCheckout.value = INVALID_FORMAT_NAME_EMPTY
            email.value.isNullOrEmpty() -> _invalidCheckout.value = INVALID_FORMAT_EMAIL_EMPTY
            phone.value.isNullOrEmpty() -> _invalidCheckout.value = INVALID_FORMAT_PHONE_EMPTY
            address.value.isNullOrEmpty() -> _invalidCheckout.value = INVALID_FORMAT_ADDRESS_EMPTY
            shippingTime.isEmpty() -> _invalidCheckout.value = INVALID_FORMAT_TIME_EMPTY
            paymentMethod.value == PaymentMethod.CASH_ON_DELIVERY -> _invalidCheckout.value = NOT_SUPPORT_CASH_ON_DELIVERY
            !isCanGetPrime -> _invalidCheckout.value = CREDIT_CART_FORMAT_INCORRECT
            isCanGetPrime -> {
                _status.value = LoadApiStatus.LOADING
                tpdCard?.getPrime()
            }
            else -> _invalidCheckout.value = NO_ONE_KNOWS
        }
    }

    /**
     * Checkout the order when everything is ready, but we still have to check the user login status haha
     * @param prime: The prime key is the token from [TPDCard]
     */
    fun checkout(prime: String) {
        when (UserManager.isLoggedIn) {
            true -> {
                UserManager.userToken?.let {
                    postOrderCheckout(
                        it,
                        OrderDetail(
                            prime,
                            Order(
                                "delivery",
                                "credit_card",
                                totalPrice.value ?: 0,
                                totalFreight.value ?: 0,
                                totalOrderPrice.value ?: 0,
                                Recipient(
                                    name.value ?: "",
                                    phone.value ?: "",
                                    email.value ?: "",
                                    address.value ?: "",
                                    shippingTime
                                ),
                                products.value.toOrderProductList()
                            )
                        )
                    )
                }
            }
            else -> {
                _navigateToLogin.value = true
                _status.value = LoadApiStatus.DONE
            }
        }
    }

    fun onCheckoutCompleted() {
        _checkoutSuccess.value = null
    }

    /**
     * track [StylishRepository.checkoutOrder]: -> [DefaultStylishRepository] : [StylishRepository] -> [StylishRemoteDataSource] : [StylishDataSource]
     * @param token: Stylish token
     * @param orderDetail: The order details will be taken on the body of http request
     */
    private fun postOrderCheckout(token: String, orderDetail: OrderDetail) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = stylishRepository.checkoutOrder(token, orderDetail)

            _checkoutSuccess.value = when (result) {
                is Result.Success -> {
                    stylishRepository.clearProductInCart()
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    _invalidCheckout.value = CHECKOUT_FAIL
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
        }
    }

    fun navigateToCheckoutSuccess() {
        _navigateToCheckoutSuccess.value = true
    }

    fun onCheckoutSuccessNavigated() {
        _navigateToCheckoutSuccess.value = null
    }

    fun onLoginNavigated() {
        _navigateToLogin.value = null
    }

    // it will occur when get prime success
    private val tpdTokenSuccessCallback = { token: String, _: TPDCardInfo, _: String ->
        checkout(token)
    }

    // it will occur when get prime failure
    private val tpdTokenFailureCallback = { status: Int, reportMsg: String ->
        tpdErrorMessage = status.toString() + reportMsg
        _status.value = LoadApiStatus.ERROR
        _invalidCheckout.value = CREDIT_CART_PRIME_FAIL
    }

    /**
     * Set up the [TPDForm]
     */
    fun setupTpd(tpdForm: TPDForm) {

        TPDSetup.initInstance(
            StylishApplication.instance,
            getString(R.string.tp_app_id).toInt(),
            getString(R.string.tp_app_key),
            TPDServerType.Sandbox
        )

        tpdErrorMessage = getString(R.string.tpd_general_error)
        isCanGetPrime = false

        tpdForm.setTextErrorColor(getColor(R.color.red_d0021b))
        tpdForm.setOnFormUpdateListener { tpdStatus ->
            when {
                tpdStatus.cardNumberStatus == TPDStatus.STATUS_ERROR ->
                    tpdErrorMessage = getString(R.string.tpd_card_number_error)
                tpdStatus.expirationDateStatus == TPDStatus.STATUS_ERROR ->
                    tpdErrorMessage = getString(R.string.tpd_expiration_date_error)
                tpdStatus.ccvStatus == TPDStatus.STATUS_ERROR ->
                    tpdErrorMessage = getString(R.string.tpd_ccv_error)
                !tpdStatus.isCanGetPrime ->
                    tpdErrorMessage = getString(R.string.tpd_general_error)
            }
            isCanGetPrime = tpdStatus.isCanGetPrime
        }

        tpdCard = TPDCard.setup(tpdForm)
            .onSuccessCallback(tpdTokenSuccessCallback)
            .onFailureCallback(tpdTokenFailureCallback)
    }

    companion object {

        const val INVALID_FORMAT_NAME_EMPTY = 0x11
        const val INVALID_FORMAT_EMAIL_EMPTY = 0x12
        const val INVALID_FORMAT_EMAIL_NOT_INCORRECT = 0x13
        const val INVALID_FORMAT_PHONE_EMPTY = 0x14
        const val INVALID_FORMAT_PHONE_INCORRECT = 0x15
        const val INVALID_FORMAT_ADDRESS_EMPTY = 0x16
        const val INVALID_FORMAT_TIME_EMPTY = 0x17

        const val NOT_SUPPORT_CASH_ON_DELIVERY = 0x18
        const val CREDIT_CART_FORMAT_INCORRECT = 0x19
        const val CREDIT_CART_PRIME_FAIL = 0x20

        const val NO_ONE_KNOWS = 0x21

        const val CHECKOUT_FAIL = 0x22
    }
}
