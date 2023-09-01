package app.appworks.school.stylish.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Wayne Chen in Jul. 2019.
 */
@Parcelize
data class CheckoutOrderResult(
    val data: OrderSuccess? = null,
    val error: String? = null
) : Parcelable

@Parcelize
data class OrderSuccess(
    val number: String
) : Parcelable
