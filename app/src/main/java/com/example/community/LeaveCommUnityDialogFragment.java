package com.example.community;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LeaveCommUnityDialogFragment extends DialogFragment {

    public static final String REQUEST_KEY = "leave_community_request";
    public static final String RESULT_CONFIRMED = "confirmed";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(requireContext())
                .setMessage("Are you sure you want to leave this CommUnity event?")
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                .setPositiveButton("Leave", (d, w) -> {
                    Bundle result = new Bundle();
                    result.putBoolean(RESULT_CONFIRMED, true);
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                })
                .create();
    }
}
