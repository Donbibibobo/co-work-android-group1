package app.appworks.school.stylish.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.databinding.FragmentDetailBinding
import app.appworks.school.stylish.ext.getVmFactoryWithContext
import app.appworks.school.stylish.util.ABtest
import app.appworks.school.stylish.util.ABtest.wishlist

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class DetailFragment : Fragment() {

    /**
     * Lazily initialize our [DetailViewModel].
     */
    private val viewModel by viewModels<DetailViewModel> {
        getVmFactoryWithContext(
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
        binding.buttonDetailMessage.isEnabled = false
        binding.buttonDetailMessage.alpha = 0.3f

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        //activate star button
        if (ABtest.version == "A"){
            binding.buttonStarred
            binding.buttonUnstarred
        } else {
            binding.buttonStarred
            binding.buttonUnstarred
        }

        val starredBtn = binding.buttonStarred
        val unStarredBtn = binding.buttonUnstarred

        if (wishlist.any { it.id == viewModel.product.value?.id }) {
            unStarredBtn.visibility = GONE
            starredBtn.visibility = VISIBLE
        } else {
            unStarredBtn.visibility = VISIBLE
            starredBtn.visibility = GONE
        }


        unStarredBtn.setOnClickListener {
            unStarredBtn.visibility = View.GONE
            starredBtn.visibility = View.VISIBLE
            viewModel.add2Wishlist(viewModel.product.value!!)
            Log.i("STARRED3", wishlist.toString())
        }

        starredBtn.setOnClickListener {
            starredBtn.visibility = View.GONE
            unStarredBtn.visibility = View.VISIBLE
            viewModel.removeFromWishlist(viewModel.product.value!!)
            Log.i("STARRED2", wishlist.toString())
        }


        binding.recyclerDetailGallery.adapter = DetailGalleryAdapter()
        binding.recyclerDetailCircles.adapter = DetailCircleAdapter()
        binding.recyclerDetailColor.adapter = DetailColorAdapter()

        /*----------------add Detail Message Adapter------------------*/
        val detailMessageAdapter = DetailMessageAdapter()
        val messageList = viewModel.message.value
        val editMessage = binding.messageInput.text
        binding.recyclerDetailMessage.adapter = detailMessageAdapter


        binding.messageInput.doAfterTextChanged {
            if (it.toString() != "") {
                binding.buttonDetailMessage.isEnabled = true
                binding.buttonDetailMessage.alpha = 1f
            } else {
                binding.buttonDetailMessage.isEnabled = false
                binding.buttonDetailMessage.alpha = 0.3f
            }
        }

        binding.buttonDetailMessage.setOnClickListener {
            Log.i("editMessage", "$editMessage")
            detailMessageAdapter.submitList(messageList)
            viewModel.reviewSubmit(editMessage.toString())
            detailMessageAdapter.notifyDataSetChanged()
        }

        viewModel.message.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                detailMessageAdapter.submitList(messageList)
            }
        })
        /*----------------add Detail Message Adapter------------------*/


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
