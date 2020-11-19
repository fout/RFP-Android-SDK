package jp.fout.rfp.android.demo.kotlin.app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.Context
import jp.fout.rfp.android.demo.kotlin.app.vo.AdResponse
import jp.fout.rfp.android.sdk.outstream.RFPOutstreamVideoAdLoader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutstreamAdRepository @Inject constructor() {
    fun getAds(context: Context, spotId: String)
            : LiveData<AdResponse<String>> {
        val data = MutableLiveData<AdResponse<String>>()

        val loader = RFPOutstreamVideoAdLoader.Builder(spotId)
                .onSuccess { item ->
                    val response = AdResponse.success(item)
                    data.postValue(response)
                }
                .onFailure { message ->
                    val response = AdResponse.error(message, null)
                    data.postValue(response)
                }
                .build()
        loader.loadAd(context)

        return data
    }
}
