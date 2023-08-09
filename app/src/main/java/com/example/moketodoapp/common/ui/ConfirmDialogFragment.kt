package com.example.moketodoapp.common.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs

/**
 * Created by K.Kobayashi on 2023/06/14.
 */
class ConfirmDialogFragment : DialogFragment() {
    private val args: ConfirmDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity()).apply {
            setMessage(args.message)
            setPositiveButton(android.R.string.ok, listener)
            setNegativeButton(android.R.string.cancel, listener)
        }.create()
    }

    private val listener = DialogInterface.OnClickListener { _, button ->
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(BUNDLE_KEY to button)
        )
    }

    companion object {
        private val TAG = ConfirmDialogFragment::class.java.simpleName
        const val REQUEST_KEY = "confirm_dialog"
        const val BUNDLE_KEY = "button_id"
    }
}
