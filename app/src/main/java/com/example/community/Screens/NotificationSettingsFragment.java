package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;
import com.example.community.User;
import com.example.community.UserService;
import com.google.android.gms.tasks.Tasks;

public class NotificationSettingsFragment extends Fragment {

    private UserService userService;

    private RadioGroup resultsRadioGroup;
    private RadioButton yesResults;
    private RadioButton noResults;
    private Button confirmButton;
    private Button cancelButton;

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

        resultsRadioGroup = view.findViewById(R.id.results_radio_group);
        yesResults        = view.findViewById(R.id.yes_results);
        noResults         = view.findViewById(R.id.no_results);
        confirmButton     = view.findViewById(R.id.confirm_button);
        cancelButton      = view.findViewById(R.id.cancel_popup);

        // 1) Load current setting from Firestore and update the radios
        loadCurrentNotificationSetting();

        // 2) Confirm: save to Firestore then go back
        confirmButton.setOnClickListener(v -> saveSettingAndGoBack());

        // 3) Cancel: just go back
        cancelButton.setOnClickListener(v ->
                NavHostFragment.findNavController(NotificationSettingsFragment.this)
                        .popBackStack()
        );
    }

    private void loadCurrentNotificationSetting() {
        String deviceToken = userService.getDeviceToken();
        if (deviceToken == null || deviceToken.isEmpty()) {
            // fallback: default to "Yes" or "No" however you like
            noResults.setChecked(true);
            return;
        }

        // Get userId from device token, then load the User to read the flag
        userService.getUserIDByDeviceToken(deviceToken)
                .onSuccessTask(userId -> userService.getByUserID(userId))
                .addOnSuccessListener(user -> {
                    if (user == null) {
                        // No user? Default to "No"
                        noResults.setChecked(true);
                        return;
                    }

                    // Adjust this getter name if your User class uses a different one
                    Boolean enabled = user.getReceiveNotifications(); // or user.getNotificationsEnabled()

                    if (enabled != null && enabled) {
                        yesResults.setChecked(true);
                    } else {
                        noResults.setChecked(true);
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(getContext(),
                            "Failed to load notification setting: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    // sensible default
                    noResults.setChecked(true);
                });
    }

    private void saveSettingAndGoBack() {
        String deviceToken = userService.getDeviceToken();
        if (deviceToken == null || deviceToken.isEmpty()) {
            Toast.makeText(getContext(),
                    "No device token – cannot save setting",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        boolean wantsNotifications = yesResults.isChecked();

        // Optional: disable button to avoid double taps
        confirmButton.setEnabled(false);

        userService.getUserIDByDeviceToken(deviceToken)
                .onSuccessTask(userId -> {
                    if (wantsNotifications) {
                        return userService.enableNotifications(userId);
                    } else {
                        return userService.disableNotifications(userId);
                    }
                })
                .addOnSuccessListener(v -> {
                    Toast.makeText(getContext(),
                            "Notification preference saved",
                            Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(NotificationSettingsFragment.this)
                            .popBackStack();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    confirmButton.setEnabled(true);
                    Toast.makeText(getContext(),
                            "Failed to save setting: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}
