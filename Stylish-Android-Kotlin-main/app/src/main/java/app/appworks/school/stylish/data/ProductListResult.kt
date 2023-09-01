package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * Created by Wayne Chen in Jul. 2019.
 */
@Parcelize
data class ProductListResult(
    val error: String? = null,
    @Json(name = "data") val products: List<Product>? = null,
    @Json(name = "next_paging") val paging: String? = null
) : Parcelable
