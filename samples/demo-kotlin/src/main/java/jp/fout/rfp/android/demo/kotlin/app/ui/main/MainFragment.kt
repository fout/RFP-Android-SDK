package jp.fout.rfp.android.demo.kotlin.app.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.fout.rfp.android.demo.kotlin.app.R
import jp.fout.rfp.android.demo.kotlin.app.di.Injectable
import jp.fout.rfp.android.demo.kotlin.app.testing.OpenForTesting
import jp.fout.rfp.android.demo.kotlin.app.util.LoadingIdlingResource
import jp.fout.rfp.android.demo.kotlin.app.vo.AdResponse
import jp.fout.rfp.android.demo.kotlin.app.vo.AdStatus
import jp.fout.rfp.android.sdk.RFP
import jp.fout.rfp.android.sdk.model.RFPInstreamInfoModel
import timber.log.Timber
import javax.inject.Inject

@OpenForTesting
class MainFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ArticleViewModel

    private lateinit var viewAdapter: ArticleAdapter

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val idlingResource: LoadingIdlingResource = LoadingIdlingResource()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        viewAdapter = ArticleAdapter(requireActivity())

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

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ArticleViewModel::class.java)

        initializeArticles()

        initializeAds()
    }

    private fun initializeArticles() {
        viewModel.articles.observe(this, Observer { articles ->
            Timber.d("Articles loaded")
            articles?.let {
                viewAdapter.articles = articles
                updateArticleView()
            }
        })
    }

    private fun initializeAds() {
        val args = MainFragmentArgs.fromBundle(arguments ?: throw AssertionError())
        val mediaId = args.adMediaId
        val spotId = args.adSpotId
        Timber.d("mediaId = $mediaId, spotId = $spotId")

        RFP.init(requireContext(), mediaId.toString())

        observeAdViewModel(viewModel.ads)

        val positions = listOf(1, 5, 9)
        viewModel.getAds(spotId, positions.size, positions)
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
        idlingResource.idle = true
    }
}
