package jp.fout.rfp.android.demo.kotlin.app.vo

import java.util.*

data class Article(
        val id: Int,
        val date: Date,
        val link: String,
        val title: String,
        val excerpt: String,
        val content: String,
        val featuredMediaUrl: String?
)
