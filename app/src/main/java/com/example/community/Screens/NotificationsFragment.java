package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;

public class NotificationsFragment extends Fragment {

    ImageButton notificationSettingsButton;
    Button backButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View notificationFragment = inflater.inflate(R.layout.notification_page, container, false);
        return notificationFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notificationSettingsButton = view.findViewById(R.id.notificationSettings);
        backButton = view.findViewById(R.id.backToEntrantHome);

        notificationSettingsButton.setOnClickListener(v ->
                NavHostFragment.findNavController(NotificationsFragment.this)
                        .navigate(R.id.NotificationSettingsFragment)
        );

        backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(NotificationsFragment.this)
                        .navigate(R.id.EntrantHomeFragment)
        );

    }

}
