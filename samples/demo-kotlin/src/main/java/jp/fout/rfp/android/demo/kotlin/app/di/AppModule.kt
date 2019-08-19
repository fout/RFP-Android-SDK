package jp.fout.rfp.android.demo.kotlin.app.di

import dagger.Module
import dagger.Provides
import jp.fout.rfp.android.demo.kotlin.app.service.WordPressService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideWordpressService(): WordPressService {
        return Retrofit.Builder()
                .baseUrl("https://backyard.fout.co.jp")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WordPressService::class.java)
    }
}
