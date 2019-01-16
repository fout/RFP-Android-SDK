package jp.fout.rfp.android.demo.kotlin.app.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import jp.fout.rfp.android.demo.kotlin.app.repository.ArticleRepository
import jp.fout.rfp.android.demo.kotlin.app.vo.Article
import timber.log.Timber

class ArticleViewModel : ViewModel() {
//    private val articles = MutableLiveData<List<ArticleRepository.ARTICLE>>()
    fun getArticles(): LiveData<List<Article>> {
//        if (articles.value == null) {
//            loadArticles()
//        }
//        return articles
        return loadArticles()
    }

    private fun loadArticles(): LiveData<List<Article>> {
        Timber.d("loadArticles")
        val repository = ArticleRepository()
        return repository.getArticles()
    }
}
