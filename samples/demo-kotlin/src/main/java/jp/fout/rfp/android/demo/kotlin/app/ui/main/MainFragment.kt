package jp.fout.rfp.android.demo.kotlin.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var viewAdapter: ArticleAdapter? = null

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

    override fun onDestroyView() {
        super.onDestroyView()
        viewAdapter?.detach()
        viewAdapter = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ArticleViewModel::class.java)

        initializeArticles()

        initializeAds()
    }

    private fun initializeArticles() {
        viewModel.articles.observe(viewLifecycleOwner, { articles ->
            Timber.d("Articles loaded")
            articles?.let {
                viewAdapter?.articles = articles
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
        data.observe(viewLifecycleOwner, Observer { response ->
            Timber.d("Ads loaded")
            response ?: return@Observer
            if (response.status == AdStatus.ERROR) {
                Timber.d("Failed to request ads: %s", response.message!!)
                return@Observer
            }
            val ads = response.data ?: return@Observer
            ads.forEach { ad ->
                Timber.d("ad position=%d, title=\"%s\"", ad.position(), ad.title())
                viewAdapter?.ads?.add(ad)
            }
            Timber.d("articles.size=%d, ads.size=%d",
                    viewAdapter?.articles?.size, viewAdapter?.ads?.size)
            if (!viewAdapter?.ads.isNullOrEmpty() && viewAdapter?.articles?.isNotEmpty() == true) {
                Timber.d("refresh with loaded ads")
                updateArticleView()
            }
        })
    }

    private fun updateArticleView() {
        val newItems = mutableListOf<Any>()
        viewAdapter?.articles?.let {
            newItems.addAll(it)
        }
        viewAdapter?.ads?.forEach { ad ->
            newItems.add(ad.position(), ad)
        }
        viewAdapter?.submitList(newItems)
        idlingResource.idle = true
    }
}
