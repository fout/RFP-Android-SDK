package jp.fout.rfp.android.demo.kotlin.app.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.fout.rfp.android.demo.kotlin.app.MainActivity

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
