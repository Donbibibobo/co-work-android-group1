package app.appworks.school.stylish

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import app.appworks.school.stylish.databinding.ActivityMainBinding
import app.appworks.school.stylish.databinding.BadgeBottomBinding
import app.appworks.school.stylish.databinding.NavHeaderDrawerBinding
import app.appworks.school.stylish.dialog.MessageDialog
import app.appworks.school.stylish.ext.getVmFactory2
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.util.CurrentFragmentType
import app.appworks.school.stylish.util.DrawerToggleType
import app.appworks.school.stylish.util.Logger
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class MainActivity : BaseActivity() {

    /**
     * Lazily initialize our [MainViewModel].
     */
    val viewModel by viewModels<MainViewModel> { getVmFactory2() }

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    // get the height of status bar from system
    private val statusBarHeight: Int
        get() {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return when {
                resourceId > 0 -> resources.getDimensionPixelSize(resourceId)
                else -> 0
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        // to show hide the shipment
        viewModel.version.observe(this) {
            if (it == "B") {
                binding.shipment.visibility = View.GONE
            } else {
                binding.shipment.visibility = View.VISIBLE
            }
        }








        installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView,
                View.ROTATION,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 2000L

            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.doOnEnd {
                startActivity(Intent(this, LogoActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                })
                splashScreenView.remove()
            }

            splashScreenView.setBackgroundColor(Color.TRANSPARENT)
            // Run your animation.
            slideUp.start()
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // observe current fragment change, only for show info
        viewModel.currentFragmentType.observe(
            this,
            Observer {
                Logger.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                Logger.i("[${viewModel.currentFragmentType.value}]")
                Logger.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            }
        )

        viewModel.navigateToLoginSuccess.observe(
            this,
            Observer {
                it?.let {
                    findNavController(R.id.myNavHostFragment).navigate(
                        NavigationDirections.navigateToMessageDialog(MessageDialog.MessageType.LOGIN_SUCCESS)
                    )
                    viewModel.onLoginSuccessNavigated()

                    // navigate to profile after login success
                    when (viewModel.currentFragmentType.value) {
                        CurrentFragmentType.PAYMENT -> {}
                        else -> viewModel.navigateToProfileByBottomNav(it)
                    }
                }
            }
        )

        binding.imageToolbarMagnifying.setOnClickListener {
            findNavController(R.id.myNavHostFragment).navigate(R.id.navigate_to_chat_fragment)
        }

        viewModel.navigateToProfileByBottomNav.observe(
            this,
            Observer {
                it?.let {
                    binding.bottomNavView.selectedItemId = R.id.navigation_profile
                    viewModel.onProfileNavigated()
                }
            }
        )

        viewModel.navigateToHomeByBottomNav.observe(
            this,
            Observer {
                it?.let {
                    binding.bottomNavView.selectedItemId = R.id.navigation_home
                    viewModel.onHomeNavigated()
                }
            }
        )

        setupToolbar()
        setupBottomNav()
        setupDrawer()
        setupNavController()
    }

    /**
     * Set up [BottomNavigationView], add badge view through [BottomNavigationMenuView] and [BottomNavigationItemView]
     * to display the count of Cart
     */
    private fun setupBottomNav() {
        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToHomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_catalog -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCatalogFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_cart -> {

                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCartFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {

                    when (viewModel.isLoggedIn) {
                        true -> {
                            findNavController(R.id.myNavHostFragment).navigate(
                                NavigationDirections.navigateToProfileFragment(
                                    viewModel.user.value
                                )
                            )
                        }
                        false -> {
                            findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToLoginDialog())
                            return@setOnItemSelectedListener false
                        }
                    }
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

        val menuView = binding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(2) as BottomNavigationItemView
        val bindingBadge = BadgeBottomBinding.inflate(LayoutInflater.from(this), itemView, true)
        bindingBadge.lifecycleOwner = this
        bindingBadge.viewModel = viewModel
    }

    /**
     * Set up [NavController.addOnDestinationChangedListener] to record the current fragment, it better than another design
     * which is change the [CurrentFragmentType] enum value by [MainViewModel] at [onCreateView]
     */
    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.homeFragment -> CurrentFragmentType.HOME
                R.id.catalogFragment -> CurrentFragmentType.CATALOG
                R.id.cartFragment -> CurrentFragmentType.CART
                R.id.profileFragment -> CurrentFragmentType.PROFILE
                R.id.detailFragment -> CurrentFragmentType.DETAIL
                R.id.paymentFragment -> CurrentFragmentType.PAYMENT
                R.id.checkoutSuccessFragment -> CurrentFragmentType.CHECKOUT_SUCCESS
                R.id.wishlistFragment -> CurrentFragmentType.WISHLIST
                R.id.historyFragment -> CurrentFragmentType.HISTORY
                R.id.chatFragment -> CurrentFragmentType.CHATBOX
                else -> viewModel.currentFragmentType.value
            }
        }
    }

    /**
     * Set up the layout of [Toolbar], according to whether it has cutout
     */
    private fun setupToolbar() {

        binding.toolbar.setPadding(0, statusBarHeight, 0, 0)

        launch {

            val dpi = resources.displayMetrics.densityDpi.toFloat()
            val dpiMultiple = dpi / DisplayMetrics.DENSITY_DEFAULT

            val cutoutHeight = getCutoutHeight()

            Logger.i("====== ${Build.MODEL} ======")
            Logger.i("$dpi dpi (${dpiMultiple}x)")
            Logger.i("statusBarHeight: ${statusBarHeight}px/${statusBarHeight / dpiMultiple}dp")

            when {
                cutoutHeight > 0 -> {
                    Logger.i("cutoutHeight: ${cutoutHeight}px/${cutoutHeight / dpiMultiple}dp")

                    val oriStatusBarHeight = resources.getDimensionPixelSize(R.dimen.height_status_bar_origin)

                    binding.toolbar.setPadding(0, oriStatusBarHeight, 0, 0)
                    val layoutParams = Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT)
                    layoutParams.gravity = Gravity.CENTER

                    when (Build.MODEL) {
                        "Pixel 5" -> { Logger.i("Build.MODEL is ${Build.MODEL}") }
                        else -> { layoutParams.topMargin = statusBarHeight - oriStatusBarHeight }
                    }
                    binding.imageToolbarLogo.layoutParams = layoutParams
                    binding.textToolbarTitle.layoutParams = layoutParams
                }
            }
            Logger.i("====== ${Build.MODEL} ======")
        }
    }

    /**
     * Set up [androidx.drawerlayout.widget.DrawerLayout] with [androidx.appcompat.widget.Toolbar]
     */
    private fun setupDrawer() {

        // set up toolbar
        val navController = this.findNavController(R.id.myNavHostFragment)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.drawerNavView, navController)

        binding.drawerLayout.fitsSystemWindows = true
        binding.drawerLayout.clipToPadding = false

        actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

                when (UserManager.isLoggedIn) { // check user login status when open drawer
                    true -> {
                        viewModel.checkUser()
                    }
                    else -> {
                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToLoginDialog())
                        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            binding.drawerLayout.closeDrawer(GravityCompat.START)
                        }
                    }
                }
            }
        }.apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }

        // Set up header of drawer ui using data binding
        val bindingNavHeader = NavHeaderDrawerBinding.inflate(
            LayoutInflater.from(this), binding.drawerNavView, false
        )

        bindingNavHeader.lifecycleOwner = this
        bindingNavHeader.viewModel = viewModel
        binding.drawerNavView.addHeaderView(bindingNavHeader.root)

        // Observe current drawer toggle to set the navigation icon and behavior
        viewModel.currentDrawerToggleType.observe(
            this,
            Observer { type ->

                actionBarDrawerToggle?.isDrawerIndicatorEnabled = type.indicatorEnabled
                supportActionBar?.setDisplayHomeAsUpEnabled(!type.indicatorEnabled)
                binding.toolbar.setNavigationIcon(
                    when (type) {
                        DrawerToggleType.BACK -> R.drawable.toolbar_back
                        else -> R.drawable.toolbar_menu
                    }
                )
                actionBarDrawerToggle?.setToolbarNavigationClickListener {
                    when (type) {
                        DrawerToggleType.BACK -> onBackPressed()
                        else -> {
                        }
                    }
                }
            }
        )
    }

    /**
     * override back key for the drawer design
     */
    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

