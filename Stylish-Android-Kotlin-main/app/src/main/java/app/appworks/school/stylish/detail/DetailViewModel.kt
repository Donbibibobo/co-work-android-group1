package app.appworks.school.stylish.detail

import android.app.Application
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.ProductList
import app.appworks.school.stylish.data.UserTrackingRequestBody
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.network.ReviewStylishApi
import app.appworks.school.stylish.network.UserStylishApi
import app.appworks.school.stylish.network.adapterWishList
import app.appworks.school.stylish.util.ABtest
import app.appworks.school.stylish.util.ABtest.wishlist
import app.appworks.school.stylish.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [DetailFragment].
 */
class DetailViewModel(
    private val stylishRepository: StylishRepository,
    private val arguments: Product,
    private val application: Application
) : AndroidViewModel(application) {
    /*----------------add Detail Message fun------------------*/
    private val _message = MutableLiveData<List<String?>>()
    val message: LiveData<List<String?>>
        get() = _message
    /*----------------add Detail Message fun------------------*/
    // Detail has product data from arguments
    private val _product = MutableLiveData<Product>().apply {
        value = arguments
    }

    val product: LiveData<Product>
        get() = _product

    val productSizesText: LiveData<String> = product.map {
        when (it.sizes.size) {
            0 -> ""
            1 -> it.sizes.first()
            else -> StylishApplication.instance.getString(
                R.string._dash_,
                it.sizes.first(),
                it.sizes.last()
            )
        }
    }

    // it for gallery circles design
    private val _snapPosition = MutableLiveData<Int>()

    val snapPosition: LiveData<Int>
        get() = _snapPosition

    // Handle navigation to Add2cart
    private val _navigateToAdd2cart = MutableLiveData<Product>()

    val navigateToAdd2cart: LiveData<Product>
        get() = _navigateToAdd2cart

    // Handle leave detail
    private val _leaveDetail = MutableLiveData<Boolean>()

    val leaveDetail: LiveData<Boolean>
        get() = _leaveDetail

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
                outRect.left =
                    StylishApplication.instance.resources.getDimensionPixelSize(R.dimen.space_detail_circle)
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
        _message.value = arguments.reviews
        Log.i("asdfasdf", "${_message.value}")
    }

    /**
     * When the gallery scroll, at the same time circles design will switch.
     */
    fun onGalleryScrollChange(
        layoutManager: RecyclerView.LayoutManager?,
        linearSnapHelper: LinearSnapHelper
    ) {
        val snapView = linearSnapHelper.findSnapView(layoutManager)
        snapView?.let {
            layoutManager?.getPosition(snapView)?.let {
                if (it != snapPosition.value) {
                    _snapPosition.value = it
                }
            }
        }
    }

    fun navigateToAdd2cart(product: Product) {
        _navigateToAdd2cart.value = product
    }

    fun onAdd2cartNavigated() {
        _navigateToAdd2cart.value = null
    }

    fun leaveDetail() {
        _leaveDetail.value = true
    }

    //set up star function
    fun isStarred(product: Product): Boolean {
        return wishlist.any { it.id == product.id }
    }

    fun add2Wishlist(product: Product) {

        if (!isStarred(product)) {
            wishlist.add(product)
            wishListFile()
        }
    }


    fun removeFromWishlist(product: Product) {
        if (isStarred(product)) {
            wishlist.remove(product)
            wishListFile()
        }
    }

    var versionAB = ABtest.version.toCharArray()[0]

    private fun wishListFile() {
        Log.i("wishListFile", "wishListFile called")

        val wishListFileName = "wishList.txt"
        val productList = ProductList(wishlist)
        val wishListJson = adapterWishList.toJson(productList)

        application?.openFileOutput(wishListFileName, Context.MODE_PRIVATE).use {
            it?.write(wishListJson.toByteArray())
        }

    }

    private fun userTrackingApiCollect(action: String, productId: String) {
        viewModelScope.launch {
            // TODO collect

            val eventDetail = JSONObject()
            val checkoutItemArray = JSONArray()

            eventDetail.put("action", action)
            eventDetail.put("collect_item", productId)

            Log.i("ABtest", "eventDetail: ${eventDetail.toString()}")


            val request = UserTrackingRequestBody(ABtest.userId, "collect", eventDetail.toString(), ABtest.getCurrentDateTime(), ABtest.version)
            val response = UserStylishApi.retrofitService.userTracking(request)
            Log.i("userTracking", "[collect]: ${response.message}")
            Log.i("userTracking", "[collect_content]: $request")
        }
    }


    /*---------------- Review Submit POST API fun------------------*/
    fun reviewSubmit(editMessage: String){
        viewModelScope.launch {
            ReviewStylishApi.retrofitService.reviewSubmit(ABtest.userId, _product.value!!.id, editMessage, ABtest.getCurrentDateTime(), ABtest.version)
            Log.i("sadsdaa" , "${editMessage}")
        }
    }
    /*---------------- Review Submit POST API fun------------------*/
}
