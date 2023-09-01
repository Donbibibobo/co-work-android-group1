package app.appworks.school.stylish.catalog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import app.appworks.school.stylish.catalog.item.CatalogItemFragment

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class CatalogAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return CatalogItemFragment(CatalogTypeFilter.values()[position])
    }

    override fun getCount() = CatalogTypeFilter.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return CatalogTypeFilter.values()[position].value
    }
}
