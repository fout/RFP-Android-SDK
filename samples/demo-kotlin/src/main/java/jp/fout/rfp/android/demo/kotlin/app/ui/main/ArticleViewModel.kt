package jp.fout.rfp.android.demo.kotlin.app.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import jp.fout.rfp.android.demo.kotlin.app.repository.AdRepository
import jp.fout.rfp.android.demo.kotlin.app.repository.ArticleRepository
import jp.fout.rfp.android.demo.kotlin.app.testing.OpenForTesting
import jp.fout.rfp.android.demo.kotlin.app.util.AbsentLiveData
import jp.fout.rfp.android.demo.kotlin.app.vo.AdResponse
import jp.fout.rfp.android.demo.kotlin.app.vo.Article
import jp.fout.rfp.android.sdk.model.RFPInstreamInfoModel
import javax.inject.Inject

@OpenForTesting
class ArticleViewModel @Inject constructor(
        app: Application,
        repository: ArticleRepository,
        adRepository: AdRepository
): AndroidViewModel(app) {
    val articles: LiveData<List<Article>> = repository.getArticles()

    private val adParameters = MutableLiveData<AdParameters>()

    val ads: LiveData<AdResponse<List<RFPInstreamInfoModel>>> = Transformations
            .switchMap(adParameters) { params ->
                if (params.spotId.isNotEmpty()) {
                    adRepository.getAds(getApplication(), params.spotId, params.count, params.positions)
                } else {
                    AbsentLiveData.create()
                }
            }

    fun getAds(spotId: String, count: Int, positions: List<Int>?) {
        adParameters.value = AdParameters(spotId, count, positions)
    }

    private data class AdParameters(val spotId: String, val count: Int, val positions: List<Int>?)
}
