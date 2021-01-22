package jp.fout.rfp.android.demo.kotlin.app

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import jp.fout.rfp.android.demo.kotlin.app.ui.main.AdParametersViewModel
import jp.fout.rfp.android.demo.kotlin.app.ui.main.MainFragmentArgs
import jp.fout.rfp.android.demo.kotlin.app.ui.main.PreferencesDialogFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.container)
        setupWithNavController(bottomNavigationView, navController)

        adParametersViewModel = ViewModelProvider(this, viewModelFactory)
                .get(AdParametersViewModel::class.java)
        adParametersViewModel.parameters.observe(this, { parameters ->
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

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}
