package jp.fout.rfp.android.demo.kotlin.app.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.fout.rfp.android.demo.kotlin.app.ui.main.MainFragment
import jp.fout.rfp.android.demo.kotlin.app.ui.main.PreferencesDialogFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributePreferencesDialogFragment(): PreferencesDialogFragment
}
