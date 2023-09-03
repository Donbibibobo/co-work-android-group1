package app.appworks.school.stylish.wishlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.data.Color
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.Variant
import app.appworks.school.stylish.databinding.FragmentWishlistBinding

class WishlistFragment : Fragment() {

    private lateinit var viewModel: WishlistViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        return inflater.inflate(R.layout.fragment_wishlist, container, false)
        val binding = FragmentWishlistBinding.inflate(inflater, container, false)

        val onClickListener = WishlistAdapter.OnClickListener { productId ->
            val action = WishlistFragmentDirections.navigateToDetailFragment(productId)
            findNavController().navigate(action)
        }

        val adapter = WishlistAdapter(onClickListener)
        binding.wishlistRecyclerView.adapter = adapter
        adapter.submitList(mockDataList)




        return binding.root
    }

    private val mockDataList = listOf(
        Product(
            id = 1,
            title = "Sample Product",
            description = "This is a sample product description.",
            price = 1999,
            texture = "Cotton",
            wash = "Machine wash",
            place = "Made in USA",
            note = "Sample product note.",
            story = "Sample product story.",
            colors = listOf(
                Color("Red", "000000"),
                Color("Blue", "65AB65")
            ),
            sizes = listOf("Small", "Medium", "Large"),
            variants = listOf(
                Variant("Red", "Small", 10),
                Variant("Red", "Medium", 15),
                Variant("Red", "Large", 20),
                Variant("Blue", "Small", 8),
                Variant("Blue", "Medium", 12),
                Variant("Blue", "Large", 18)
            ),
            mainImage =  "https://api.appworks-school.tw/assets/201807242222/main.jpg",
            images = listOf(
                "https://api.appworks-school.tw/assets/201807242222/main.jpg"
            )
        )
    )
}