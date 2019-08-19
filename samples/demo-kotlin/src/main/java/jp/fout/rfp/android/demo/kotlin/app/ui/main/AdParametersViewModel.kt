package jp.fout.rfp.android.demo.kotlin.app.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class AdParametersViewModel @Inject constructor(): ViewModel() {
    val parameters = MutableLiveData<AdParameters>()

    data class AdParameters(val mediaId: Int, val spotId: String)
}
