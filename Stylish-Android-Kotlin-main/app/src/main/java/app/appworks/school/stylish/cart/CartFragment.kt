package app.appworks.school.stylish.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.databinding.FragmentCartBinding
import app.appworks.school.stylish.ext.getVmFactory

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class CartFragment : Fragment() {

    /**
     * Lazily initialize our [CartViewModel].
     */
    private val viewModel by viewModels<CartViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        init()
        val binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.recyclerCart.adapter = CartAdapter(viewModel.uiState)

        binding.layoutSwipeRefreshCart.setOnRefreshListener {
            binding.recyclerCart.adapter?.notifyDataSetChanged()
            binding.layoutSwipeRefreshCart.isRefreshing = false
        }

        viewModel.navigateToPayment.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    findNavController().navigate(CartFragmentDirections.navigateToPaymentFragment())
                    viewModel.onPaymentNavigated()
                }
            }
        )

        return binding.root
    }

//    private fun init() {
//        activity?.let {
//            ViewModelProviders.of(it).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = CurrentFragmentType.CART
//            }
//        }
//    }
}
