package app.appworks.school.stylish.add2cart

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.DialogAdd2cartBinding
import app.appworks.school.stylish.dialog.MessageDialog
import app.appworks.school.stylish.ext.getVmFactory
import app.appworks.school.stylish.ext.setTouchDelegate
import app.appworks.school.stylish.util.Logger

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class Add2cartDialog : AppCompatDialogFragment() {

    /**
     * Lazily initialize our [Add2cartViewModel].
     */
    private val viewModel by viewModels<Add2cartViewModel> { getVmFactory(Add2cartDialogArgs.fromBundle(requireArguments()).productKey) }
    private lateinit var binding: DialogAdd2cartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Add2CartDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogAdd2cartBinding.inflate(inflater, container, false)
        binding.layoutAdd2cart.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_slide_up))

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.buttonAdd2cartClose.setTouchDelegate()
        binding.recyclerAdd2cartColorSelector.adapter = Add2cartColorAdapter(viewModel)

        viewModel.navigateToAddedSuccess.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    findNavController().navigate(NavigationDirections.navigateToMessageDialog(MessageDialog.MessageType.ADDED_SUCCESS))
                    viewModel.onAddedSuccessNavigated()
                }
            }
        )

        viewModel.navigateToAddedFail.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    findNavController().navigate(
                        NavigationDirections.navigateToMessageDialog(
                            MessageDialog.MessageType.MESSAGE.apply { value.message = getString(R.string.product_exist) }
                        )
                    )
                    viewModel.onAddedFailNavigated()
                }
            }
        )

        viewModel.amount.observe(
            viewLifecycleOwner,
            Observer {
                Logger.d("amount=$it")
            }
        )

        viewModel.leave.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    dismiss()
                    viewModel.onLeaveCompleted()
                }
            }
        )

        return binding.root
    }

    override fun dismiss() {
        binding.layoutAdd2cart.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_slide_down))
        Handler().postDelayed({ super.dismiss() }, 200)
    }

    fun leave() { dismiss() }

    fun nothing() {}
}
