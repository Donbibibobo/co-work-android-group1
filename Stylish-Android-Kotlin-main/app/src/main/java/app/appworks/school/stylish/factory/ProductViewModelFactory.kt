package app.appworks.school.stylish.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.stylish.add2cart.Add2cartViewModel
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.detail.DetailViewModel

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Factory for all ViewModels which need [Product].
 */
@Suppress("UNCHECKED_CAST")
class ProductViewModelFactory(
    private val stylishRepository: StylishRepository,
    private val product: Product
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(DetailViewModel::class.java) ->
                    DetailViewModel(stylishRepository, product)

                isAssignableFrom(Add2cartViewModel::class.java) ->
                    Add2cartViewModel(stylishRepository, product)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
