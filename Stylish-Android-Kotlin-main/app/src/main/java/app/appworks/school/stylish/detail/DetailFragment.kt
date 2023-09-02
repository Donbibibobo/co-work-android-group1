package app.appworks.school.stylish.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.data.DetailMessage
import app.appworks.school.stylish.databinding.FragmentDetailBinding
import app.appworks.school.stylish.ext.getVmFactory

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class DetailFragment : Fragment() {

    /**
     * Lazily initialize our [DetailViewModel].
     */
    private val viewModel by viewModels<DetailViewModel> { getVmFactory(DetailFragmentArgs.fromBundle(requireArguments()).productKey) }

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

        binding.recyclerDetailGallery.adapter = DetailGalleryAdapter()
        binding.recyclerDetailCircles.adapter = DetailCircleAdapter()
        binding.recyclerDetailColor.adapter = DetailColorAdapter()
        /*----------------add Detail Message Adapter------------------*/
        val detailMessageAdapter = DetailMessageAdapter()
        val messageList = viewModel.messageMockData
        binding.recyclerDetailMessage.adapter = detailMessageAdapter


        detailMessageAdapter.submitList(messageList.value)

        binding.buttonDetailMessage.setOnClickListener {
            val editMessage = binding.messageInput.text
            Log.i("editMessage", "$editMessage")
            viewModel.addMockMessage(editMessage.toString())
            detailMessageAdapter.submitList(messageList.value)
            detailMessageAdapter.notifyDataSetChanged()
        }
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
                    (binding.recyclerDetailCircles.adapter as DetailCircleAdapter).selectedPosition.value = (it % product.images.size)
                }
            )
        }

        viewModel.navigateToAdd2cart.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    findNavController().navigate(NavigationDirections.navigateToAdd2cartDialog(it))
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
