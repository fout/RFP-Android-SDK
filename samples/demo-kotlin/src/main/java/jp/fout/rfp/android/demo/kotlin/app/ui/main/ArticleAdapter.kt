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
import android.view.View
import android.view.ViewGroup
import jp.fout.rfp.android.demo.kotlin.app.R
import jp.fout.rfp.android.demo.kotlin.app.databinding.CardAdBinding
import jp.fout.rfp.android.demo.kotlin.app.databinding.CardAdVideoBinding
import jp.fout.rfp.android.demo.kotlin.app.databinding.CardArticleBinding
import jp.fout.rfp.android.demo.kotlin.app.util.VisibilityTracker
import jp.fout.rfp.android.demo.kotlin.app.vo.Article
import jp.fout.rfp.android.sdk.model.RFPInstreamInfoModel
import timber.log.Timber
import java.util.*

class ArticleAdapter(activity: Activity, callback: DiffUtil.ItemCallback<Any>)
    : ListAdapter<Any, RecyclerView.ViewHolder>(callback) {

    companion object {
        /** 表示とみなすパーセンテージ */
        const val VISIBLE_REQUIREMENT_PERCENTAGE = 50
        /** インプレッション発生とみなす時間(ミリ秒) */
        const val VIEWABLE_REQUIREMENT_MILLIS = 1000L
    }

    var articles: List<Any> = emptyList()

    var ads: MutableList<RFPInstreamInfoModel> = mutableListOf()

    private val tracker = VisibilityTracker(activity).apply {
        setVisibilityTrackerListener(object : VisibilityTracker.VisibilityTrackerListener {
            override fun onVisibilityChanged(visibleViews: List<View>, invisibleViews: List<View>) {
                handleVisibleViews(visibleViews)
            }
        })
    }
    private val viewPositionMap = WeakHashMap<View, Int>()
    private val scheduledViewMap = WeakHashMap<View, Timer>()

    private fun handleVisibleViews(visibleViews: List<View>) {
        // 表示されているViewがImp送信予定になければTimerをセットする
        for (v in visibleViews) {
            val viewPosition = viewPositionMap[v]
            val model = getItem(viewPosition!!) as RFPInstreamInfoModel
            var timer: Timer? = scheduledViewMap[v]
            if (timer == null) {
                // Schedule measure impression
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        Timber.d("Measure impression: position=%d, title=\"%s\"", model.position(), model.title())
                        // インプレッションを送信する
                        // （重複排除などは SDK がやるため、毎回通信などは発生しない）
                        onAdEventListener?.onShow(model)
                    }
                }, VIEWABLE_REQUIREMENT_MILLIS)
                scheduledViewMap[v] = timer
            }
        }
        // Imp送信予定のViewが非表示になったら送信予定を取り消す
        scheduledViewMap.filterKeys {
            !visibleViews.contains(it)
        }.forEach { (view, timer) ->
            timer.cancel()
            scheduledViewMap.remove(view)
        }
    }

    fun trackVisibility(view: View, position: Int) {
        viewPositionMap[view] = position
        tracker.addView(view, VISIBLE_REQUIREMENT_PERCENTAGE)
    }

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
                    adapter.onAdEventListener?.onClick(ad)
                }
                holder.binding.ad = ad
                adapter.trackVisibility(holder.itemView, holder.adapterPosition)
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
                    adapter.onVideoAdEventListener?.onButtonClick(ad)
                }
                holder.binding.ad = ad
                adapter.trackVisibility(holder.itemView, holder.adapterPosition)
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
        val item = getItem(position)
        return when (item) {
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

    interface OnAdEventListener {
        fun onClick(model: RFPInstreamInfoModel)
        fun onShow(model: RFPInstreamInfoModel)
    }

    var onAdEventListener: OnAdEventListener? = null

    interface OnVideoAdClickListener {
        fun onButtonClick(model: RFPInstreamInfoModel)
    }

    var onVideoAdEventListener: OnVideoAdClickListener? = null
}
