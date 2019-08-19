package jp.fout.rfp.android.demo.kotlin.app.ui.main

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import jp.fout.rfp.android.demo.kotlin.app.R
import jp.fout.rfp.android.demo.kotlin.app.databinding.CardAdBinding
import jp.fout.rfp.android.demo.kotlin.app.databinding.CardAdVideoBinding
import jp.fout.rfp.android.demo.kotlin.app.databinding.CardArticleBinding
import jp.fout.rfp.android.demo.kotlin.app.vo.Article
import jp.fout.rfp.android.sdk.instream.RFPInstreamAdEvent
import jp.fout.rfp.android.sdk.model.RFPInstreamInfoModel
import jp.fout.rfp.android.sdk.util.RFPVisibilityTracker

class ArticleAdapter(activity: Activity)
    : ListAdapter<Any, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private val visibilityTracker: RFPVisibilityTracker = RFPVisibilityTracker(activity)

    var articles: List<Any> = emptyList()

    var ads: MutableList<RFPInstreamInfoModel> = mutableListOf()

    enum class ViewType(val id: Int) {
        ARTICLE(1) {
            override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?): RecyclerView.ViewHolder {
                val binding = DataBindingUtil.inflate<CardArticleBinding>(inflater, R.layout.card_article, parent, false)
                return ArticleViewHolder(binding)
            }

            override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: Any, adapter: ArticleAdapter) {
                val article = item as Article
                holder as ArticleViewHolder
                holder.itemView.setOnClickListener {
                    openLink(holder.itemView.context, article.link)
                }
                holder.binding.article = article
            }
        },

        AD(1001) {
            override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?): RecyclerView.ViewHolder {
                val binding = DataBindingUtil.inflate<CardAdBinding>(inflater, R.layout.card_ad, parent, false)
                return AdViewHolder(binding)
            }

            override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: Any, adapter: ArticleAdapter) {
                val ad = item as RFPInstreamInfoModel
                holder as AdViewHolder
                holder.itemView.setOnClickListener {
                    RFPInstreamAdEvent.sendClickEvent(holder.itemView.context, ad)
                }
                holder.binding.ad = ad
                adapter.visibilityTracker.addView(holder.itemView, ad)
            }
        },

        AD_VIDEO(1002) {
            override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?): RecyclerView.ViewHolder {
                val binding = DataBindingUtil.inflate<CardAdVideoBinding>(inflater, R.layout.card_ad_video, parent, false)
                return AdVideoViewHolder(binding)
            }

            override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: Any, adapter: ArticleAdapter) {
                val ad = item as RFPInstreamInfoModel
                holder as AdVideoViewHolder
                holder.binding.adActionButton.setOnClickListener {
                    holder.binding.adVideo.pause()
                    RFPInstreamAdEvent.sendClickEvent(holder.itemView.context, ad)
                }
                holder.binding.ad = ad
                adapter.visibilityTracker.addView(holder.itemView, ad)
            }
        };

        companion object {
            fun forId(id: Int): ViewType {
                for (viewType: ViewType in values()) {
                    if (viewType.id == id) {
                        return viewType
                    }
                }
                throw AssertionError()
            }

            private fun openLink(context: Context, url: String) {
                val uri = Uri.parse(url)
                val tabsIntent = CustomTabsIntent.Builder()
                        .setShowTitle(true)
                        .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
                        .setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .build()
                tabsIntent.launchUrl(context, uri)
            }
        }

        abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?): RecyclerView.ViewHolder
        abstract fun bindViewHolder(holder: RecyclerView.ViewHolder, item: Any, adapter: ArticleAdapter)
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is Article -> {
                ViewType.ARTICLE.id
            }
            is RFPInstreamInfoModel -> {
                if (item.isVideo) ViewType.AD_VIDEO.id else ViewType.AD.id
            }
            else -> throw AssertionError(item.javaClass)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewType.forId(viewType).createViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        ViewType.forId(holder.itemViewType).bindViewHolder(holder, item, this)
    }

    class ArticleViewHolder(val binding: CardArticleBinding) : RecyclerView.ViewHolder(binding.root)

    class AdViewHolder(val binding: CardAdBinding) : RecyclerView.ViewHolder(binding.root)

    class AdVideoViewHolder(val binding: CardAdVideoBinding) : RecyclerView.ViewHolder(binding.root)
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Article && newItem is Article) {
            oldItem.id == newItem.id
        } else if (oldItem is RFPInstreamInfoModel && newItem is RFPInstreamInfoModel) {
            oldItem.ad_id() == newItem.ad_id()
        } else {
            false
        }
    }
}
