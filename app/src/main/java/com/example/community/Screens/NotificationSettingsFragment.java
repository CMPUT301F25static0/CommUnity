package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;

public class NotificationSettingsFragment extends Fragment {

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

        Button confirmButton = view.findViewById(R.id.confirm_button);
        Button cancelButton  = view.findViewById(R.id.cancel_popup);

        // Confirm: you can save settings, then go back
        confirmButton.setOnClickListener(v ->
                NavHostFragment.findNavController(NotificationSettingsFragment.this)
                        .popBackStack()
        );

        // Cancel: just go back to previous page
        cancelButton.setOnClickListener(v ->
                NavHostFragment.findNavController(NotificationSettingsFragment.this)
                        .popBackStack()
        );
    }
}
