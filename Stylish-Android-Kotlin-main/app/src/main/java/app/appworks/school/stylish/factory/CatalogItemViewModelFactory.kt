package app.appworks.school.stylish.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.stylish.catalog.CatalogTypeFilter
import app.appworks.school.stylish.catalog.item.CatalogItemViewModel
import app.appworks.school.stylish.data.source.StylishRepository

/**
 * Created by Wayne Chen on 2019-08-07.
 *
 * Factory for catalog item ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class CatalogItemViewModelFactory(
    private val catalogType: CatalogTypeFilter
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(CatalogItemViewModel::class.java) ->
                    CatalogItemViewModel(catalogType)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
