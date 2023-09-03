package app.appworks.school.stylish.wishlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.databinding.FragmentWishlistBinding
import app.appworks.school.stylish.util.ABtest.wishlist

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
        adapter.submitList(wishlist)




        return binding.root
    }
}