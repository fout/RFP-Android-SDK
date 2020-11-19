package jp.fout.rfp.android.demo.kotlin.app.ui.outstream

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
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
import jp.fout.rfp.android.sdk.video.VideoAdView
import timber.log.Timber
import java.lang.AssertionError
import javax.inject.Inject

@OpenForTesting
class OutstreamFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: OutstreamViewModel

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val idlingResource: LoadingIdlingResource = LoadingIdlingResource()

    private lateinit var adVideoView: VideoAdView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_outstream, container, false)

        adVideoView = view.findViewById(R.id.ad_video)
        adVideoView.setOnErrorListener { _, message, t ->
            Timber.d(t, "Video Ad View Error: $message")
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(OutstreamViewModel::class.java)

        initializeAds()
    }

    private fun initializeAds() {
        val args = OutstreamFragmentArgs.fromBundle(arguments ?: throw AssertionError())
        val mediaId = args.adMediaId
        val spotId = args.adSpotId
        Timber.d("mediaId = $mediaId, spotId = $spotId")

        RFP.init(requireContext(), mediaId.toString())

        observeAdViewModel(viewModel.ads)

        viewModel.getAds(spotId)
    }

    private fun observeAdViewModel(data: LiveData<AdResponse<String>>) {
        data.observe(viewLifecycleOwner, Observer { response ->
            Timber.d("Ads loaded")
            response ?: return@Observer
            if (response.status == AdStatus.ERROR) {
                Timber.d("Failed to request ads: %s", response.message!!)
                return@Observer
            }
            val vast = response.data ?: return@Observer
            adVideoView.processAd(vast)
        })
    }
}