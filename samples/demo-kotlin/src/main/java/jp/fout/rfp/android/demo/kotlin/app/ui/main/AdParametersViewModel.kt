package jp.fout.rfp.android.demo.kotlin.app.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class AdParametersViewModel @Inject constructor(): ViewModel() {
    val parameters = MutableLiveData<AdParameters>()

    data class AdParameters(val mediaId: Int, val spotId: String)
}
