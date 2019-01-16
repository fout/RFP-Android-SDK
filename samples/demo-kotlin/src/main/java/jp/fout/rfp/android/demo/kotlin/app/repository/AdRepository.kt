package jp.fout.rfp.android.demo.kotlin.app.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import jp.fout.rfp.android.demo.kotlin.app.vo.AdResponse
import jp.fout.rfp.android.sdk.instream.RFPInstreamAdPlacer
import jp.fout.rfp.android.sdk.instream.RFPInstreamAdPlacerListener
import jp.fout.rfp.android.sdk.model.RFPInstreamInfoModel
import timber.log.Timber

class AdRepository(private val placer: RFPInstreamAdPlacer) {
    fun getAds(): LiveData<AdResponse<List<RFPInstreamInfoModel>>> {
        val data = MutableLiveData<AdResponse<List<RFPInstreamInfoModel>>>()

        placer.setAdListener(object : RFPInstreamAdPlacerListener {
            override fun onAdsLoaded(items: MutableList<out RFPInstreamInfoModel>?) {
                Timber.d("item.size = %d", items?.size)
                val response = AdResponse.success(items)
                data.postValue(response)
            }

            override fun onAdsLoadedFail(errorString: String?) {
                val response = AdResponse.error(errorString ?: "Failed to load ads", null)
                data.postValue(response)
            }

            override fun onAdMainImageLoaded(imageUrl: String?) {
            }

            override fun onAdImageLoadedFail(imageUrl: String?, errorString: String?) {
            }

            override fun onAdClicked(redirectUrl: String?) {
            }
        })
        placer.loadAd()

        return data
    }
}
