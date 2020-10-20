package jp.fout.rfp.android.demo.kotlin.app

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import jp.fout.rfp.android.demo.kotlin.app.ui.main.AdParametersViewModel
import jp.fout.rfp.android.demo.kotlin.app.ui.main.MainFragmentArgs
import jp.fout.rfp.android.demo.kotlin.app.ui.main.PreferencesDialogFragment
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        private val DEFAULT_AD_PARAMETERS
                = AdParametersViewModel.AdParameters(2, "NDQ0OjMx")
//                = AdParametersViewModel.AdParameters(16, "MTY3OjY1")

    }

    private lateinit var adParametersViewModel: AdParametersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val navController = findNavController(R.id.container)
        setupWithNavController(bottomNavigationView, navController)

        adParametersViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(AdParametersViewModel::class.java)
        adParametersViewModel.parameters.observe(this, Observer { parameters ->
            parameters?.let {
                val args = MainFragmentArgs.Builder()
                        .setAdMediaId(it.mediaId)
                        .setAdSpotId(it.spotId)
                        .build()
                Navigation.findNavController(this@MainActivity, R.id.container)
                        .navigate(R.id.nav_item_instream, args.toBundle())
            }
        })

        if (savedInstanceState == null) {
            adParametersViewModel.parameters
                    .postValue(DEFAULT_AD_PARAMETERS)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_edit -> {
                val dialog = PreferencesDialogFragment()
                dialog.show(supportFragmentManager, "PreferencesDialogFragment")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}
