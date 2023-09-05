package app.appworks.school.stylish.add2cart

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.databinding.InverseMethod
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.data.Color
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.UserTrackingRequestBodyString
import app.appworks.school.stylish.data.Variant
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.network.UserStylishApi
import app.appworks.school.stylish.util.ABtest
import app.appworks.school.stylish.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [Add2cartDialog].
 */
class Add2cartViewModel(
    private val stylishRepository: StylishRepository,
    private val arguments: Product
) : ViewModel() {

    // Detail has product data from arguments
    private val _product = MutableLiveData<Product>().apply {
        value = arguments
    }

    val product: LiveData<Product>
        get() = _product

    var selectedColorPosition = MutableLiveData<Int>()

    val selectedColor = MutableLiveData<Color>()

    var selectedVariantPosition = MutableLiveData<Int>()

    var selectedVariant = MutableLiveData<Variant>()

    val variantsBySelectedColor: LiveData<List<Variant>?> = selectedColor.map { color ->
        color?.let {
            product.value?.variants?.filter { variant ->
                variant.colorCode == it.code
            }
        }
    }

    val amount = MutableLiveData<Long>()

    // Handle navigation to Added Success
    private val _navigateToAddedSuccess = MutableLiveData<Product>()

    val navigateToAddedSuccess: LiveData<Product>
        get() = _navigateToAddedSuccess

    // Handle navigation to Added Fail
    private val _navigateToAddedFail = MutableLiveData<Product>()

    val navigateToAddedFail: LiveData<Product>
        get() = _navigateToAddedFail

    // Handle leave add2cart
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val decoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0
            } else {
                outRect.left = StylishApplication.instance.resources.getDimensionPixelSize(R.dimen.space_detail_circle)
            }
        }
    }

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
     * track [StylishRepository.getUserProfile]: -> [DefaultStylishRepository] : [StylishRepository] -> [StylishLocalDataSource] : [StylishDataSource]
     */
    fun insertToCart() {
        product.value?.let {
            coroutineScope.launch {
                selectedVariant.value?.apply {
                    it.selectedVariant = this
                    it.amount = amount.value
                    if (stylishRepository.isProductInCart(it.id, it.selectedVariant.colorCode, it.selectedVariant.size)) {

                        _navigateToAddedFail.value = it
                    } else {
                        stylishRepository.insertProductInCart(it)
                        _navigateToAddedSuccess.value = it
                    }

                    // TODO add_to_car
                    val request = UserTrackingRequestBodyString(ABtest.userId, "add_to_cart", it.id.toString(), ABtest.getCurrentDateTime(), ABtest.version)
                    val response = UserStylishApi.retrofitService.userTrackingPoly(request)
                    Log.i("userTracking", "[add_to_cart]: ${response.message}")
                    Log.i("userTracking", "[add_to_cart_content]: $request")
                }
            }
        }
    }

    fun onAddedSuccessNavigated() {
        _navigateToAddedSuccess.value = null
    }

    fun onAddedFailNavigated() {
        _navigateToAddedFail.value = null
    }

    fun selectColor(color: Color, position: Int) {
        Logger.w("selectColor=$color, position=$position")
        selectedVariantPosition.value = null
        selectedVariant.value = null
        selectedColor.value = color
        selectedColorPosition.value = position
    }

    fun selectSize(variant: Variant, position: Int) {
        Logger.w("selectSize=$variant, position=$position")
        amount.value = 1
        selectedVariant.value = variant
        selectedVariantPosition.value = position
    }

    fun increaseAmount() {
        Logger.w("increaseAmount()")
        amount.value = amount.value?.plus(1)
    }

    fun decreaseAmount() {
        Logger.w("decreaseAmount()")
        amount.value = amount.value?.minus(1)
    }

    @InverseMethod("convertLongToString")
    fun convertStringToLong(value: String): Long {
        return try {
            value.toLong().let {
                when (it) {
                    0L -> 1
                    else -> it
                }
            }
        } catch (e: NumberFormatException) {
            1
        }
    }

    fun convertLongToString(value: Long): String {
        return value.toString()
    }

    fun leave() {
        _leave.value = true
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }

    fun nothing() {}
}
