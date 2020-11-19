package jp.fout.rfp.android.demo.kotlin.app

import android.app.Application
import android.os.Build
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import jp.fout.rfp.android.demo.kotlin.app.di.AppInjector
import timber.log.Timber
import javax.inject.Inject

class DemoKotlinApp : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        // see https://github.com/square/leakcanary/issues/1081
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            LeakCanary.install(this)
        }

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}
