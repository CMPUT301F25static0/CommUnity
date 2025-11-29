package com.example.community.Screens;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DeleteAccountConfirmDialogFragment extends DialogFragment {

    public static final String REQUEST_KEY = "delete_account_request";
    public static final String RESULT_CONFIRMED = "confirmed";
    /**
     * DialogFragment that asks the user to confirm deletion of their account.
     * Sends the result back to the parent fragment via setFragmentResult.
     */

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(requireContext())
                .setMessage("Are you sure you want to delete your CommUnity account?")
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                .setPositiveButton("Delete", (d, w) -> {
                    Bundle result = new Bundle();
                    result.putBoolean(RESULT_CONFIRMED, true);
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                })
                .create();
    }
}