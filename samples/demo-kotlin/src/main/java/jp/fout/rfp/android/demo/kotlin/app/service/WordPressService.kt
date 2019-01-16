package jp.fout.rfp.android.demo.kotlin.app.service

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

interface WordPressService {
    @GET("wp-json/wp/v2/posts?_embed")
    fun listArticles(): Call<List<Article>>

    data class Article(
            var id: Int,
            var date: String,
            var title: Title,
            var link: String,
            var content: Content,
            var excerpt: Excerpt,
            @SerializedName("_embedded") var embedded: Embedded?
    )

    data class Title(
            var rendered: String
    )

    data class Content(
            var rendered: String,
            var protected: Boolean
    )

    data class Excerpt(
            var rendered: String,
            var protected: Boolean
    )

    data class Embedded(
            @SerializedName("wp:featuredmedia") var featuredMedia: List<FeaturedMedia>?
    )

    data class FeaturedMedia(
            @SerializedName("source_url") var sourceUrl: String?
    )
}
