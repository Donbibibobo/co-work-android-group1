package app.appworks.school.stylish.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Created by Wayne Chen in Jul. 2019.
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class Variant(
    @ColumnInfo(name = "product_selected_color_code")
    @Json(name = "color_code") val colorCode: String,
    @ColumnInfo(name = "product_selected_size")
    val size: String,
    val stock: Int
) : Parcelable
