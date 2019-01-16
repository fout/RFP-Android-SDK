package jp.fout.rfp.android.demo.kotlin.app.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import jp.fout.rfp.android.demo.kotlin.app.ui.main.AdViewModel
import java.lang.IllegalArgumentException

class AdViewModelFactory(
        val context: Context,
        private val spotId: String,
        private val count: Int,
        private val positions: List<Int>?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == AdViewModel::class.java) {
            return AdViewModel(context, spotId, count, positions) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
