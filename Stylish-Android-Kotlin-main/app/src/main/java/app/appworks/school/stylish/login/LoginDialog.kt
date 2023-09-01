package app.appworks.school.stylish.login

import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.stylish.MainViewModel
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.DialogLoginBinding
import app.appworks.school.stylish.ext.getVmFactory
import app.appworks.school.stylish.ext.setTouchDelegate
import com.facebook.login.LoginManager

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class LoginDialog : AppCompatDialogFragment() {

    /**
     * Lazily initialize our [LoginViewModel].
     */
    private val viewModel by viewModels<LoginViewModel> { getVmFactory() }
    private lateinit var binding: DialogLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.LoginDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogLoginBinding.inflate(inflater, container, false)
        binding.layoutLogin.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_slide_up))

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.buttonLoginClose.setTouchDelegate()

        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        viewModel.user.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    mainViewModel.setupUser(it)
                }
            }
        )

        viewModel.navigateToLoginSuccess.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    mainViewModel.navigateToLoginSuccess(it)
                    dismiss()
                }
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

        viewModel.loginFacebook.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
                    viewModel.onLoginFacebookCompleted()
                }
            }
        )

        return binding.root
    }

    override fun dismiss() {
        binding.layoutLogin.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_slide_down))
        Handler().postDelayed({ super.dismiss() }, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.fbCallbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
