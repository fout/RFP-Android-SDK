package jp.fout.rfp.android.demo.kotlin.app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import jp.fout.rfp.android.demo.kotlin.app.service.WordPressService
import jp.fout.rfp.android.demo.kotlin.app.vo.Article
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
        private val service: WordPressService
) {
    fun getArticles(): LiveData<List<Article>> {
        val data = MutableLiveData<List<Article>>()

        val api = service.listArticles()
        api.enqueue(object : Callback<List<WordPressService.Article>> {
            override fun onResponse(
                    call: Call<List<WordPressService.Article>>,
                    response: retrofit2.Response<List<WordPressService.Article>>) {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val articles = mutableListOf<Article>()
                response.body()?.forEach { item ->
                    val article = Article(
                            item.id,
                            sdf.parse(item.date),
                            item.link,
                            item.title.rendered,
                            Jsoup.parse(item.excerpt.rendered).text(),
                            item.content.rendered,
                            item.embedded?.featuredMedia?.firstOrNull()?.sourceUrl
                    )
//                    Timber.d(article.toString())
                    articles.add(article)
                }
                data.postValue(articles)
            }

            override fun onFailure(call: Call<List<WordPressService.Article>>, t: Throwable) {
                Timber.d(t, "onFailure")
            }
        })

        return data
    }

}
