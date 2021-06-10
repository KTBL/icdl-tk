package de.ktbl.eikotiger.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import de.ktbl.android.sharedlibrary.livedata.valueOrDefault
import de.ktbl.android.sharedlibrary.util.Nullable
import de.ktbl.android.sharedlibrary.view.activity.IBarTitleSetable
import de.ktbl.eikotiger.BuildConfig
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.ActivityMainBinding
import de.ktbl.eikotiger.view.viewmodel.RecordingStateVM
import timber.log.Timber
import javax.inject.Inject

//should also extend SupportFragmentActivity extends AbstractBaseActivity
class MainActivity : SupportFragmentActivity(), NavigationView.OnNavigationItemSelectedListener,
    IBarTitleSetable {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var rootView: View? = null
    private var showingSplashScreen = false
    private val navigationController: NavController
        get() {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
            return navHostFragment.navController
        }

    /**
     * Labels of NavDestinations used while recording and accessible by the bottom navigation
     * bar. This list is used for handling back navigation
     * @see [onBackPressed]
     */
    private val recordingRootDestLabels = listOf(
        "RecordingOverviewFragment",
        "fragment_indicator_info",
        "indicatorOptionList"
    )
    private val recordingStartDestLabel = "RecordingOverviewFragment"

    private lateinit var recordingStateVM: RecordingStateVM

    override fun getRoot(): View {
        return rootView!!
    }

    override fun hideBar() {
        val bar = this.supportActionBar ?: return
        bar.hide()
    }

    override fun showBar() {
        val bar = this.supportActionBar ?: return
        bar.show()
    }

    override fun onBackPressed() {
        val drawer = drawer
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (recordingStateVM.isRecordingActive.valueOrDefault(false)) {
            // In case a recording session is active we want to intercept the back button pressed
            // if one of the NavDestinations available in the bottom navigation bar is active.
            // Then instead of simply back navigating the proper recording exit must be handled.
            // Else just normally work with the back stack.
            val currentDestLabel = navigationController.currentDestination?.label ?: ""
            if (recordingRootDestLabels.contains(currentDestLabel)) {
                this.handleRecordingExitRequest()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun lockDrawer(locked: Boolean) {
        val drawer = drawer
        if (locked) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    private val drawer: DrawerLayout
        get() = findViewById(R.id.drawer_layout)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Navigation-actions taken in here are always(!) global-navigations
        // Non-global actions should handled elsewhere!
        var actionId = NO_ACTION_ID_SET
        when (item.itemId) {
            R.id.nav_main_overview  -> actionId = R.id.action_global_homeFragment
            R.id.nav_main_prod_dir  -> actionId = R.id.action_global_prodDirListFragment
            R.id.nav_main_settings  -> actionId = R.id.action_global_settingsActivity
            R.id.nav_main_recording -> actionId = R.id.action_global_recordingSetupFragment
            R.id.nav_main_impressum -> actionId = R.id.action_global_impressFragment
            R.id.nav_main_export    -> actionId = R.id.action_global_exportFragment
            else                    -> Timber.tag(TAG).e(
                "onNavigationItemSelected got request to unknown navigation to %s.",
                item.title
            )
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return if (actionId == NO_ACTION_ID_SET) {
            Timber.tag(TAG)
                .w("onNavigationItemSelected has not set an target actionId, therefore no navigation will be done!")
            Snackbar.make(
                rootView!!,
                String.format(
                    "Navigation zu %s ist leider noch nicht möglich.",
                    item.title
                ),
                Snackbar.LENGTH_SHORT
            )
                .show()
            false
        } else {
            navigationController.navigate(actionId)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        val value = Nullable.empty<Boolean>()
        if (id == R.id.main_action_info) {
            val navController = navigationController
            val bundle = Bundle()
            // TODO: Get the UUID of the Info-Bundle from .. the Database or the Resource System?
            bundle.putString("infoUUID", "")
            navController.navigate(R.id.action_global_infoActivity, bundle)
            value.set(true)
        }
        if (value.isNull) {
            value.set(super.onOptionsItemSelected(item))
        }
        if (BuildConfig.DEBUG && value.isNull) {
            throw AssertionError("To be returned value should never be null at this point!")
        }
        return value.get()
    }

    private fun onRecordingNavigationItemSelected(item: MenuItem): Boolean {
        if (!recordingStateVM.isRecordingActive.valueOrDefault(false)) {
            Timber.tag(TAG)
                .w("onRecordingNavigationItemSelect even though isRecordingActive is false, dismissing the event!")
            return false
        }
        val currentDestLabel = navigationController.currentDestination?.label ?: ""
        when (item.itemId) {
            R.id.recording_exit                    -> handleRecordingExitRequest()
            R.id.recording_indicator_list          -> {
                if (currentDestLabel != recordingRootDestLabels[0]) {
                    recordingStateVM.navigateToOverview()
                }
            }
            R.id.recording_current_indicator_info  -> {
                if (currentDestLabel != recordingRootDestLabels[1]) {
                    recordingStateVM.navigateToIndicatorInfo()
                }
            }
            R.id.recording_current_indicator_input -> {
                if (currentDestLabel != recordingRootDestLabels[2]) {
                    recordingStateVM.navigateToInput()
                }
            }
        }

        return false
    }

    /**
     * Checks whether the user really wants to exit the Recording session. If yes this function also
     * tears down any navigation and activity related changes previously set up for a recording
     * session and notifies other components.
     */
    private fun handleRecordingExitRequest() {
        if (!recordingStateVM.isRecordingActive.valueOrDefault(false)) {
            Timber.tag(TAG).w("handleRecordingExit called while isRecordingActive is false!")
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Wollen Sie die Erhebung wirklich beenden?")
            setPositiveButton("Ja") { _, _ ->
                // TODO: Add proper exit handling here!!
                //       The according RecordingManager functions should be called for a proper shutdown.
                this@MainActivity.recordingStateVM.isRecordingActive.value = false
                navigationController.navigate(R.id.action_global_homeFragment)
            }
            setNegativeButton("Nein") { _, _ ->
                // Nothing to do in this case
            }
        }
        builder.create().show()
    }

    override fun setBarTitle(title: CharSequence?) {
        this.supportActionBar?.title = title
    }

    override fun setBarTitle(resId: Int) {
        this.supportActionBar?.setTitle(resId)
    }

    override fun getBarTitle(): CharSequence? {
        return this.supportActionBar?.title
    }

    override fun resetBarTitle() {
        this.supportActionBar?.title = ""
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onNavigated(
        navController: NavController, navDestination: NavDestination,
        arguments: Bundle?
    ) {
        val destinationLabel: String = if (navDestination.label == null) {
            ""
        } else {
            navDestination.label.toString()
        }
        if (this.getString(R.string.lbl_fragment_splash_screen) == destinationLabel) {
            showingSplashScreen = true
            hideBar()
            lockDrawer(true)
        } else {
            if (showingSplashScreen) {
                showBar()
                lockDrawer(false)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = ActivityMainBinding.inflate(this.layoutInflater)
        this.binding.lifecycleOwner = this
        this.rootView = binding.root
        setContentView(rootView)

        val toolbar = findViewById<Toolbar>(
            R.id.toolbar
        )
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = this.binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        val recordingNavigationView = this.binding.mainContent.recordingNavigationBar
        recordingNavigationView.setOnNavigationItemReselectedListener {
            this.onRecordingNavigationItemSelected(it)
        }
        navigationController.addOnDestinationChangedListener { navController: NavController, navDestination: NavDestination, arguments: Bundle? ->
            onNavigated(
                navController,
                navDestination,
                arguments
            )
        }

        this.recordingStateVM = ViewModelProvider(
            this,
            this.viewModelFactory
        ).get(RecordingStateVM::class.java)
        this.recordingStateVM.isRecordingActive.observe(this, this::onIsRecordingActiveStateChanged)
        this.binding.mainstate = this.recordingStateVM
        this.binding.mainContent.recordingNavigationBar.setOnNavigationItemSelectedListener(this::onRecordingNavigationItemSelected)

    }


    private fun onIsRecordingActiveStateChanged(state: Boolean) {
        lockDrawer(state)
        if (state) {
            taggedTimber.i("Recording is now active")

        }
    }


    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val taggedTimber = Timber.tag(TAG)
        private const val NO_ACTION_ID_SET = Int.MIN_VALUE
    }
}