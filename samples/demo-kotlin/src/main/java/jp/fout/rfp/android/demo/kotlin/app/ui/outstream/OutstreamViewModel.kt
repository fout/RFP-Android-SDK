package jp.fout.rfp.android.demo.kotlin.app.ui.outstream

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import jp.fout.rfp.android.demo.kotlin.app.repository.OutstreamAdRepository
import jp.fout.rfp.android.demo.kotlin.app.testing.OpenForTesting
import jp.fout.rfp.android.demo.kotlin.app.util.AbsentLiveData
import jp.fout.rfp.android.demo.kotlin.app.vo.AdResponse
import javax.inject.Inject

@OpenForTesting
class OutstreamViewModel @Inject constructor(
        app: Application,
        adRepository: OutstreamAdRepository
): AndroidViewModel(app) {
    private val adParameters = MutableLiveData<AdParameters>()

    val ads: LiveData<AdResponse<String>> = Transformations
            .switchMap(adParameters) { params ->
                if (params.spotId.isNotEmpty()) {
                    adRepository.getAds(getApplication(), params.spotId)
                } else {
                    AbsentLiveData.create()
                }
            }

    fun getAds(spotId: String) {
        adParameters.value = AdParameters(spotId)
    }

    private data class AdParameters(val spotId: String)
}
