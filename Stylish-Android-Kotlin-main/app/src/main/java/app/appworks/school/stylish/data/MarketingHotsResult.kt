package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * Created by Wayne Chen in Jul. 2019.
 */
@Parcelize
data class MarketingHotsResult(
    val error: String? = null,
    @Json(name = "data") val hotsList: List<Hots>? = null
) : Parcelable {

    fun toHomeItems(): List<HomeItem> {
        val items = mutableListOf<HomeItem>()

        hotsList?.let {
            for ((title, products) in it) {
                items.add(HomeItem.Title(title))
                for ((index, product) in products.withIndex()) {
                    when (index % 2) {
                        0 -> items.add(HomeItem.FullProduct(product))
                        1 -> items.add(HomeItem.CollageProduct(product))
                    }
                }
            }
        }
        return items
    }
}
