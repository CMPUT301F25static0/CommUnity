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
import com.example.community.UserService;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    ImageButton notificationSettingsButton;
    Button backButton;
    RecyclerView notificationList;

    private ArrayList<Notification> notifications;
    private NotificationAdapter notificationAdapter;

    private NotificationService notificationService;
    private UserService userService;

    // store the current userId here once we resolve it
    private String currentUserId;

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

        notifications = new ArrayList<>();
        notificationService = new NotificationService();
        userService = new UserService();

        // RecyclerView
        notificationAdapter = new NotificationAdapter(
                notifications,
                new NotificationAdapter.NotificationActionListener() {
                    @Override
                    public void onAccept(Notification notification) {
                        // Guard: make sure we actually have a userId
                        if (currentUserId == null || currentUserId.isEmpty()) {
                            Toast.makeText(getContext(),
                                    "User not loaded yet. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        notificationService.respondToInvitation(
                                        notification.getEventID(),
                                        currentUserId,
                                        true
                                )
                                .addOnSuccessListener(v -> {
                                    Toast.makeText(getContext(),
                                            "Invitation accepted",
                                            Toast.LENGTH_SHORT).show();
                                    notificationAdapter.removeNotification(notification);
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(getContext(),
                                                "Failed to accept invitation",
                                                Toast.LENGTH_SHORT).show()
                                );

                    }

                    @Override
                    public void onDecline(Notification notification) {
                        // Same guard here
                        if (currentUserId == null || currentUserId.isEmpty()) {
                            Toast.makeText(getContext(),
                                    "User not loaded yet. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        notificationService.respondToInvitation(
                                        notification.getEventID(),
                                        currentUserId,
                                        false
                                )
                                .addOnSuccessListener(v -> {
                                    Toast.makeText(getContext(),
                                            "Invitation declined",
                                            Toast.LENGTH_SHORT).show();
                                    notificationAdapter.removeNotification(notification);
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(getContext(),
                                                "Failed to decline invitation",
                                                Toast.LENGTH_SHORT).show()
                                );
                    }

                    @Override
                    public void onViewEvent(Notification notification) {
                        Bundle bundle = new Bundle();
                        bundle.putString("eventID", notification.getEventID());
                        NavHostFragment.findNavController(NotificationsFragment.this)
                                .navigate(R.id.action_NotificationsFragment_to_EventDetailsFragment, bundle);
                    }
                });

        notificationList.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationList.setAdapter(notificationAdapter);

        // Load notifications
        loadNotificationsForCurrentUser();

        notificationSettingsButton.setOnClickListener(v ->
                NavHostFragment.findNavController(NotificationsFragment.this)
                        .navigate(R.id.action_EntrantNotificationsFragment_to_NotificationSettingsFragment)
        );

        backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(NotificationsFragment.this)
                        .popBackStack()
        );
    }

    private void loadNotificationsForCurrentUser() {
        String deviceToken = userService.getDeviceToken();
        if (deviceToken == null || deviceToken.isEmpty()) {
            Toast.makeText(getContext(),
                    "Device token not found – cannot load notifications",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        userService.getUserIDByDeviceToken(deviceToken)
                .addOnSuccessListener(userId -> {
                    // Store userId so onAccept/onDecline can use it
                    currentUserId = userId;

                    notificationService.listUserNotification(userId, 50, null)
                            .addOnSuccessListener(fetchedNotifications -> {
                                notifications.clear();
                                notifications.addAll(fetchedNotifications);
                                notificationAdapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                                Toast.makeText(getContext(),
                                        "Failed to load notifications: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(getContext(),
                            "Failed to resolve user from device token: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}
