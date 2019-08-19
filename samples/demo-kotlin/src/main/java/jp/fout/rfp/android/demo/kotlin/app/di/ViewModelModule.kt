package jp.fout.rfp.android.demo.kotlin.app.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import jp.fout.rfp.android.demo.kotlin.app.ui.main.AdParametersViewModel
import jp.fout.rfp.android.demo.kotlin.app.ui.main.ArticleViewModel
import jp.fout.rfp.android.demo.kotlin.app.viewmodel.DemoKotlinViewModelFactory

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ArticleViewModel::class)
    abstract fun bindArticleViewModel(articleViewModel: ArticleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AdParametersViewModel::class)
    abstract fun bindAdParametersViewModel(adParametersViewModel: AdParametersViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: DemoKotlinViewModelFactory): ViewModelProvider.Factory
}
