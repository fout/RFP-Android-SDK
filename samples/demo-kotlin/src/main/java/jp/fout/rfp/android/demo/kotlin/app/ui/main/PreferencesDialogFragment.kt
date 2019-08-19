package jp.fout.rfp.android.demo.kotlin.app.ui.main

import android.app.Dialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import jp.fout.rfp.android.demo.kotlin.app.R
import jp.fout.rfp.android.demo.kotlin.app.di.Injectable
import javax.inject.Inject

class PreferencesDialogFragment : DialogFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(AdParametersViewModel::class.java)

        val view = View.inflate(context, R.layout.fragment_preferences_dialog, null)
        val textMediaId = view.findViewById<TextInputEditText>(R.id.textMediaId).apply {
            setText(viewModel.parameters.value?.mediaId.toString())
        }
        val textSpotId = view.findViewById<TextInputEditText>(R.id.textSpotId).apply {
            setText(viewModel.parameters.value?.spotId)
        }
        return AlertDialog.Builder(requireContext()).setView(view)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val mediaId = textMediaId.text.toString().toInt()
                    val spotId = textSpotId.text.toString()
                    viewModel.parameters.postValue(
                            AdParametersViewModel.AdParameters(mediaId, spotId)
                    )
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }
}
