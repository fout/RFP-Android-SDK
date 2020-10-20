package jp.fout.rfp.android.demo.kotlin.app.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.fout.rfp.android.demo.kotlin.app.ui.main.MainFragment
import jp.fout.rfp.android.demo.kotlin.app.ui.main.PreferencesDialogFragment
import jp.fout.rfp.android.demo.kotlin.app.ui.outstream.OutstreamFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributeOutstreamFragment(): OutstreamFragment

    @ContributesAndroidInjector
    abstract fun contributePreferencesDialogFragment(): PreferencesDialogFragment
}
