package app.appworks.school.stylish.data

/**
 * Created by Wayne Chen in Jul. 2019.
 */
sealed class HomeItem {

    abstract val id: Long

    data class Title(val title: String) : HomeItem() {
        override val id: Long = -1
    }
    data class FullProduct(val product: Product) : HomeItem() {
        override val id: Long
            get() = product.id
    }
    data class CollageProduct(val product: Product) : HomeItem() {
        override val id: Long
            get() = product.id
    }
}
