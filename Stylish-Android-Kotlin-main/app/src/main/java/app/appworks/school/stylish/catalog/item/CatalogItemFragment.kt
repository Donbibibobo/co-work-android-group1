package app.appworks.school.stylish.catalog.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.R
import app.appworks.school.stylish.catalog.CatalogTypeFilter
import app.appworks.school.stylish.catalog.item.compose.CatalogItemScreen
import app.appworks.school.stylish.ext.getVmFactory
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class CatalogItemFragment(private val catalogType: CatalogTypeFilter) : Fragment() {

    /**
     * Lazily initialize our [CatalogItemViewModel].
     */
    private val viewModel by viewModels<CatalogItemViewModel> { getVmFactory(catalogType) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    val pagingItems = viewModel.catalog.collectAsLazyPagingItems()
                    val state = rememberSwipeRefreshState(false)

                    SwipeRefresh(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                        onRefresh = { pagingItems.refresh() },
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            @Suppress("KotlinConstantConditions")
                            when (pagingItems.loadState.refresh) {
                                is LoadState.Loading -> {
                                    CircularProgressIndicator(
                                        color = colorResource(id = R.color.black_3f3a3a)
                                    )
                                }
                                else -> {}
                            }
                            CatalogItemScreen(pagingItems) {
                                findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
                            }
                        }
                    }
                }
            }
        }
    }
}
