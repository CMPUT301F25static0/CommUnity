package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.ArrayAdapters.NotificationAdapter;
import com.example.community.Notification;
import com.example.community.NotificationService;
import com.example.community.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class NotificationsFragment extends Fragment {

    ImageButton notificationSettingsButton;
    Button backButton;
    RecyclerView notificationList;
    NotificationAdapter notificationAdapter;
    NotificationService notificationService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notification_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notificationSettingsButton = view.findViewById(R.id.notificationSettings);
        backButton = view.findViewById(R.id.backToEntrantHome);
        notificationList = view.findViewById(R.id.notificationList);

        notificationService = new NotificationService();

        // Set up RecyclerView
        notificationAdapter = new NotificationAdapter(new NotificationAdapter.NotificationActionListener() {
            @Override
            public void onAccept(Notification notification) {
                // TODO: Update waitlist entry to ACCEPTED, etc.
                Toast.makeText(getContext(),
                        "Accepted invitation for event " + notification.getEventID(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDecline(Notification notification) {
                // TODO: Update waitlist entry / mark declined
                Toast.makeText(getContext(),
                        "Declined invitation for event " + notification.getEventID(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onViewEvent(Notification notification) {
                // TODO: Navigate to event description using notification.getEventID()
                // For now just toast:
                Toast.makeText(getContext(),
                        "View event: " + notification.getEventID(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        notificationList.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationList.setAdapter(notificationAdapter);

        loadNotificationsForCurrentUser();

        // Settings → NotificationSettingsFragment (you already have this in nav_graph)
        notificationSettingsButton.setOnClickListener(v ->
                NavHostFragment.findNavController(NotificationsFragment.this)
                        .navigate(R.id.action_EntrantNotificationsFragment_to_NotificationSettingsFragment)
        );

        // Back → previous screen (user home)
        backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(NotificationsFragment.this)
                        .popBackStack()
        );
    }

    private void loadNotificationsForCurrentUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getContext(), "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        notificationService.listUserNotification(userId, 50, null)
                .addOnSuccessListener(notifications -> {
                    notificationAdapter.setNotifications(notifications);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(),
                            "Failed to load notifications: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}
