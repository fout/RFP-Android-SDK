package jp.fout.rfp.android.demo.kotlin.app.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.fout.rfp.android.demo.kotlin.app.R
import jp.fout.rfp.android.demo.kotlin.app.ui.main.ArticleAdapter.OnAdEventListener
import jp.fout.rfp.android.demo.kotlin.app.viewmodel.AdViewModelFactory
import jp.fout.rfp.android.demo.kotlin.app.vo.AdResponse
import jp.fout.rfp.android.demo.kotlin.app.vo.AdStatus
import jp.fout.rfp.android.demo.kotlin.app.vo.Article
import jp.fout.rfp.android.sdk.RFP
import jp.fout.rfp.android.sdk.model.RFPInstreamInfoModel
import timber.log.Timber

class MainFragment : Fragment() {
    companion object {
        fun newInstance(): Fragment = MainFragment()
    }

    private lateinit var viewModel: ArticleViewModel
    private lateinit var adViewModel: AdViewModel

    private lateinit var viewAdapter: ArticleAdapter

    private var mediaId: Int? = null
    private var spotId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        viewAdapter = ArticleAdapter(activity!!, DiffCallback())

        val llm = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, llm.orientation)

        view.findViewById<RecyclerView>(R.id.listArticles)?.apply {
            setHasFixedSize(true)
            layoutManager = llm
            addItemDecoration(decoration)
            adapter = viewAdapter
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initializeArticles()

        initializeAds()
    }

    private fun initializeArticles() {
        viewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        observeViewModel(viewModel.getArticles())
    }

    private fun observeViewModel(data: LiveData<List<Article>>) {
        data.observe(this, Observer { articles ->
            Timber.d("Articles loaded")
            articles?.let {
                viewAdapter.articles = articles
                updateArticleView()
            }
        })
    }

    private fun initializeAds() {
        val adParametersViewModel = ViewModelProviders.of(activity!!)
                .get(AdParametersViewModel::class.java)
        adParametersViewModel.parameters.value?.let {
            mediaId = it.mediaId
            spotId = it.spotId
        }
        val mediaId = mediaId ?: throw AssertionError("Invalid mediaId")
        val spotId = spotId ?: throw AssertionError("Invalid spotId")

        RFP.init(context, mediaId.toString())

        val positions = listOf(1, 5, 9)
        val adViewModelFactory = activity?.let {
            AdViewModelFactory(it, spotId, positions.size, positions)
        }
        adViewModel = ViewModelProviders.of(this, adViewModelFactory).get(spotId, AdViewModel::class.java)
        viewAdapter.onAdEventListener = object : OnAdEventListener {
            override fun onClick(model: RFPInstreamInfoModel) {
                adViewModel.onClick(model)
            }
            override fun onShow(model: RFPInstreamInfoModel) {
                adViewModel.onShow(model)
            }
        }
        viewAdapter.onVideoAdEventListener = object : ArticleAdapter.OnVideoAdClickListener {
            override fun onButtonClick(model: RFPInstreamInfoModel) {
                adViewModel.onVideoButtonClick(model)
            }
        }
        observeAdViewModel(adViewModel.getAds())
    }

    private fun observeAdViewModel(data: LiveData<AdResponse<List<RFPInstreamInfoModel>>>) {
        data.observe(this, Observer { response ->
            Timber.d("Ads loaded")
            response ?: return@Observer
            if (response.status == AdStatus.ERROR) {
                Timber.d("Failed to request ads: %s", response.message!!)
                return@Observer
            }
            val ads = response.data ?: return@Observer
            ads.forEach { ad ->
                Timber.d("ad position=%d, title=\"%s\"", ad.position(), ad.title())
                viewAdapter.ads.add(ad)
            }
            Timber.d("articles.size=%d, ads.size=%d",
                    viewAdapter.articles.size, viewAdapter.ads.size)
            if (!viewAdapter.ads.isNullOrEmpty() && viewAdapter.articles.isNotEmpty()) {
                Timber.d("refresh with loaded ads")
                updateArticleView()
            }
        })
    }

    private fun updateArticleView() {
        val newItems = mutableListOf<Any>()
        newItems.addAll(viewAdapter.articles)
        viewAdapter.ads.forEach { ad ->
            newItems.add(ad.position(), ad)
        }
        viewAdapter.submitList(newItems)
    }
}

class DiffCallback : DiffUtil.ItemCallback<Any>() {
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
