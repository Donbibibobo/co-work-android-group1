package app.appworks.school.stylish.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.FragmentPaymentBinding
import app.appworks.school.stylish.ext.getVmFactory
import app.appworks.school.stylish.ext.showToast
import app.appworks.school.stylish.network.DataStylishApi
import app.appworks.school.stylish.payment.PaymentViewModel.Companion.CHECKOUT_FAIL
import app.appworks.school.stylish.payment.PaymentViewModel.Companion.CREDIT_CART_FORMAT_INCORRECT
import app.appworks.school.stylish.payment.PaymentViewModel.Companion.CREDIT_CART_PRIME_FAIL
import app.appworks.school.stylish.payment.PaymentViewModel.Companion.NOT_SUPPORT_CASH_ON_DELIVERY
import app.appworks.school.stylish.util.ABtest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class PaymentFragment : Fragment() {

    /**
     * Lazily initialize our [PaymentViewModel].
     */
    private val viewModel by viewModels<PaymentViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
//        init()
        val binding = FragmentPaymentBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerPayment.adapter = PaymentAdapter(viewModel)
        binding.viewModel = viewModel


        // AB test

        when (ABtest.version) {
            "A" -> binding.shipment.visibility = View.GONE
            "B" -> binding.shipment.visibility = View.VISIBLE
            else -> throw IllegalArgumentException("PaymentViewModel: ABtest.version doesn't exist")
        }










        viewModel.checkoutSuccess.observe(viewLifecycleOwner, Observer {

            it?.let {
                viewModel.navigateToCheckoutSuccess()
                viewModel.onCheckoutCompleted()
            }
        })

        viewModel.navigateToCheckoutSuccess.observe(viewLifecycleOwner, Observer { it ->
            viewModel.postOrderApi()
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToCheckoutSuccessFragment())
                viewModel.onCheckoutSuccessNavigated()
            }
        })

        viewModel.invalidCheckout.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    NOT_SUPPORT_CASH_ON_DELIVERY -> {
                        activity.showToast(getString(R.string.no_pay_by_cash))
                    }

                    CREDIT_CART_FORMAT_INCORRECT, CREDIT_CART_PRIME_FAIL -> {
                        activity.showToast(viewModel.tpdErrorMessage)
                    }

                    CHECKOUT_FAIL -> {
                        activity.showToast(
                            viewModel.error.value ?: getString(R.string.love_u_3000)
                        )
                    }

                    else -> {}
                }
            }
        })

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToLoginDialog())
                viewModel.onLoginNavigated()
            }
        })

        return binding.root
    }

//    private fun init() {
//        activity?.let {
//            ViewModelProviders.of(it).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = CurrentFragmentType.PAYMENT
//            }
//        }
//    }
}
