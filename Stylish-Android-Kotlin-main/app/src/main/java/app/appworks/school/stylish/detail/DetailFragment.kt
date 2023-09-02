package app.appworks.school.stylish.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.R
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.databinding.FragmentDetailBinding
import app.appworks.school.stylish.ext.getVmFactory

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class DetailFragment : Fragment() {

    /**
     * Lazily initialize our [DetailViewModel].
     */
    private val viewModel by viewModels<DetailViewModel> {
        getVmFactory(
            DetailFragmentArgs.fromBundle(
                requireArguments()
            ).productKey
        )
    }

//    private var previousCurrentFragmentType: CurrentFragmentType? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        init()
        val binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        //activate star button
        val starredBtn = binding.buttonStarred
        val unStarredBtn = binding.buttonUnstarred


        starredBtn.setOnClickListener {
            starredBtn.visibility = View.GONE
            unStarredBtn.visibility = View.VISIBLE
            viewModel.removeFromWishlist(viewModel.product.value!!)

        }

        unStarredBtn.setOnClickListener {
            unStarredBtn.visibility = View.GONE
            starredBtn.visibility = View.VISIBLE
            viewModel.add2Wishlist(viewModel.product.value!!)
        }
//
//        starredBtn.setOnClickListener {
//            if (isStarred) {
//                starredBtn.visibility = View.GONE
//                unStarredBtn.visibility = View.VISIBLE
//                wishList.remove(viewModel.product.value)
//                Log.i("STARRED1", wishList.toString())
//            }
//            isStarred = !isStarred
//        }
//
//        unStarredBtn.setOnClickListener {
//            if (!isStarred) {
//                unStarredBtn.visibility = View.GONE
//                starredBtn.visibility = View.VISIBLE
//                wishList.add(viewModel.product.value!!)
//                Log.i("STARRED2", wishList.toString())
//            }
//            isStarred = !isStarred
//        }
//
//        if (isStarred){
//            starredBtn.visibility = View.VISIBLE
//            unStarredBtn.visibility = View.GONE
//        } else {
//            starredBtn.visibility = View.GONE
//            unStarredBtn.visibility = View.VISIBLE
//        }


        binding.recyclerDetailGallery.adapter = DetailGalleryAdapter()
        binding.recyclerDetailCircles.adapter = DetailCircleAdapter()
        binding.recyclerDetailColor.adapter = DetailColorAdapter()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerDetailGallery)
        }

        binding.recyclerDetailGallery.setOnScrollChangeListener { _, _, _, _, _ ->
            viewModel.onGalleryScrollChange(
                binding.recyclerDetailGallery.layoutManager,
                linearSnapHelper
            )
        }

        // set the initial position to the center of infinite gallery
        viewModel.product.value?.let { product ->
            binding.recyclerDetailGallery
                .scrollToPosition(product.images.size * 100)

            viewModel.snapPosition.observe(
                viewLifecycleOwner,
                Observer {
                    (binding.recyclerDetailCircles.adapter as DetailCircleAdapter).selectedPosition.value =
                        (it % product.images.size)
                }
            )
        }

        viewModel.navigateToAdd2cart.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    findNavController().navigate(
                        NavigationDirections.navigateToAdd2cartDialog(
                            it
                        )
                    )
                    viewModel.onAdd2cartNavigated()
                }
            }
        )

        viewModel.leaveDetail.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    if (it) findNavController().popBackStack()
                }
            }
        )

        return binding.root
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        previousCurrentFragmentType?.let {
//            ViewModelProviders.of(activity!!).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = it
//            }
//        }
//    }

//    private fun init() {
//        activity?.let {
//            ViewModelProviders.of(it).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = CurrentFragmentType.DETAIL
//            }
//        }
//    }
}
