package jp.fout.rfp.android.demo.kotlin.app

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import jp.fout.rfp.android.demo.kotlin.app.ui.main.AdParametersViewModel
import jp.fout.rfp.android.demo.kotlin.app.ui.main.MainFragment
import jp.fout.rfp.android.demo.kotlin.app.ui.main.PreferencesDialogFragment

class MainActivity : AppCompatActivity() {
    companion object {
        private val DEFAULT_AD_PARAMETERS
                = AdParametersViewModel.AdParameters(2, "NDQ0OjMx")
//                = AdParametersViewModel.AdParameters(16, "MTY3OjY1")

    }

    private lateinit var adParametersViewModel: AdParametersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        adParametersViewModel = ViewModelProviders.of(this)
                .get(AdParametersViewModel::class.java)
        adParametersViewModel.parameters.observe(this, Observer { parameters ->
            parameters?.let {
                val fragment = MainFragment.newInstance()
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow()
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
        val id = item.itemId
        when (id) {
            R.id.menu_item_edit -> {
                val dialog = PreferencesDialogFragment()
                dialog.show(supportFragmentManager, "PreferencesDialogFragment")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
