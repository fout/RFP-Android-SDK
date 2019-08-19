package jp.fout.rfp.android.demo.kotlin.app

import android.app.Activity
import android.app.Application
import android.os.Build
import com.squareup.leakcanary.LeakCanary
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import jp.fout.rfp.android.demo.kotlin.app.di.AppInjector
import timber.log.Timber
import javax.inject.Inject

class DemoKotlinApp : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        // see https://github.com/square/leakcanary/issues/1081
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            LeakCanary.install(this)
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector
}
