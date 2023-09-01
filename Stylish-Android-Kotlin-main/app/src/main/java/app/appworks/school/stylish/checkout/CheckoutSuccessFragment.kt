package app.appworks.school.stylish.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.stylish.MainViewModel
import app.appworks.school.stylish.databinding.FragmentCheckoutSuccessBinding
import app.appworks.school.stylish.ext.getVmFactory

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class CheckoutSuccessFragment : Fragment() {

    /**
     * Lazily initialize our [CheckoutSuccessViewModel].
     */
    private val viewModel by viewModels<CheckoutSuccessViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        init()
        val binding = FragmentCheckoutSuccessBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.navigateToHome.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
                    mainViewModel.navigateToHomeByBottomNav()
                    viewModel.onHomeNavigated()
                }
            }
        )

        // Handle back key behavior to navigate to Home
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
                    mainViewModel.navigateToHomeByBottomNav()
                }
            }
        )

        return binding.root
    }

//    private fun init() {
//        activity?.let {
//            ViewModelProviders.of(it).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = CurrentFragmentType.CHECKOUT_SUCCESS
//            }
//        }
//    }
}
