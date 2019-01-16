package jp.fout.rfp.android.demo.kotlin.app.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import jp.fout.rfp.android.demo.kotlin.app.repository.AdRepository
import jp.fout.rfp.android.demo.kotlin.app.vo.AdResponse
import jp.fout.rfp.android.sdk.RFP
import jp.fout.rfp.android.sdk.model.RFPInstreamInfoModel

class AdViewModel(context: Context, spotId: String, count: Int, positions: List<Int>?) : ViewModel() {
    private val placer = RFP.createInstreamAdPlacer(context, spotId, count, positions)!!

    fun getAds(): LiveData<AdResponse<List<RFPInstreamInfoModel>>> {
        val repository = AdRepository(placer)
        return repository.getAds()
    }

    fun onShow(model: RFPInstreamInfoModel) {
        placer.measureImp(model)
    }

    fun onClick(model: RFPInstreamInfoModel) {
        placer.sendClickEvent(model)
    }

    fun onVideoButtonClick(model: RFPInstreamInfoModel) {
        placer.sendClickEvent(model)
    }
}
