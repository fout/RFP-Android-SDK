package jp.fout.rfp.android.demo.kotlin.app.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import jp.fout.rfp.android.demo.kotlin.app.vo.AdResponse
import jp.fout.rfp.android.sdk.instream.RFPInstreamAdLoader
import jp.fout.rfp.android.sdk.model.RFPInstreamInfoModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdRepository @Inject constructor() {
    fun getAds(context: Context, spotId: String, count: Int, positions: List<Int>?)
            : LiveData<AdResponse<List<RFPInstreamInfoModel>>> {
        val data = MutableLiveData<AdResponse<List<RFPInstreamInfoModel>>>()

        val loader = RFPInstreamAdLoader.Builder(spotId)
                .count(count)
                .positions(positions)
                .onSuccess { items ->
                    val response = AdResponse.success(items)
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
