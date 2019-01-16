package jp.fout.rfp.android.demo.kotlin.app.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class AdParametersViewModel : ViewModel() {
    val parameters = MutableLiveData<AdParameters>()

    data class AdParameters(val mediaId: Int, val spotId: String)
}
