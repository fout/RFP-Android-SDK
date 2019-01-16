package jp.fout.rfp.android.demo.kotlin.app

import android.app.Application
import timber.log.Timber

class DemoKotlinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
