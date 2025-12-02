package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;
import com.example.community.UserService;

public class NotificationSettingsFragment extends Fragment {

    private UserService userService;
    private RadioButton yesResultsRadio;
    private RadioButton noResultsRadio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notification_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userService = new UserService();

        Button confirmButton = view.findViewById(R.id.confirm_button);
        Button cancelButton  = view.findViewById(R.id.cancel_popup);
        yesResultsRadio      = view.findViewById(R.id.yes_results);
        noResultsRadio       = view.findViewById(R.id.no_results);

        // Confirm: save settings, then go back
        confirmButton.setOnClickListener(v -> saveSettingsAndGoBack());

        // Cancel: just go back to previous page
        cancelButton.setOnClickListener(v ->
                NavHostFragment.findNavController(NotificationSettingsFragment.this)
                        .popBackStack()
        );
    }

    private void saveSettingsAndGoBack() {
        // Figure out which option is selected
        final boolean wantsNotifications = yesResultsRadio.isChecked();

        String deviceToken = userService.getDeviceToken();
        if (deviceToken == null || deviceToken.isEmpty()) {
            Toast.makeText(getContext(),
                    "Unable to find current user device token",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Resolve userId from device token
        userService.getUserIDByDeviceToken(deviceToken)
                .addOnSuccessListener(userId -> {
                    // Enable or disable notifications
                    if (wantsNotifications) {
                        userService.enableNotifications(userId)
                                .addOnSuccessListener(v -> {
                                    Toast.makeText(getContext(),
                                            "Notifications enabled",
                                            Toast.LENGTH_SHORT).show();
                                    NavHostFragment.findNavController(NotificationSettingsFragment.this)
                                            .popBackStack();
                                })
                                .addOnFailureListener(e -> Toast.makeText(
                                        getContext(),
                                        "Failed to enable notifications",
                                        Toast.LENGTH_SHORT
                                ).show());
                    } else {
                        userService.disableNotifications(userId)
                                .addOnSuccessListener(v -> {
                                    Toast.makeText(getContext(),
                                            "Notifications disabled",
                                            Toast.LENGTH_SHORT).show();
                                    NavHostFragment.findNavController(NotificationSettingsFragment.this)
                                            .popBackStack();
                                })
                                .addOnFailureListener(e -> Toast.makeText(
                                        getContext(),
                                        "Failed to disable notifications",
                                        Toast.LENGTH_SHORT
                                ).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(
                        getContext(),
                        "Failed to load user for notification settings",
                        Toast.LENGTH_SHORT
                ).show());
    }
}
