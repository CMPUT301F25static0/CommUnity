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

/**
 * Fragment that displays notifications for the current user.
 * Users can view, accept, or decline invitations and navigate to notification settings.
 */
public class NotificationsFragment extends Fragment {

    ImageButton notificationSettingsButton;
    Button backButton;
    RecyclerView notificationList;

    private ArrayList<Notification> notifications;
    private NotificationAdapter notificationAdapter;

    private NotificationService notificationService;
    private UserService userService;

    /**
     * Inflates the notifications layout.
     *
     * @param inflater           LayoutInflater to inflate views
     * @param container          Parent view container
     * @param savedInstanceState Saved state bundle
     * @return Inflated fragment view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notification_page, container, false);
    }

    /**
     * Called after the fragment's view is created.
     * Initializes UI components, sets up RecyclerView, and loads notifications.
     *
     * @param view               The fragment's view
     * @param savedInstanceState Saved state bundle
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notificationSettingsButton = view.findViewById(R.id.notificationSettings);
        backButton = view.findViewById(R.id.backToEntrantHome);
//        notificationList = view.findViewById(R.id.notificationList);

        notifications = new ArrayList<>();
        notificationService = new NotificationService();
        userService = new UserService();

        // Initialize RecyclerView adapter
        notificationAdapter = new NotificationAdapter(
                notifications,
                new NotificationAdapter.NotificationActionListener() {
                    @Override
                    public void onAccept(Notification notification) {
                        Toast.makeText(getContext(),
                                "Accepted invitation for event " + notification.getEventID(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDecline(Notification notification) {
                        Toast.makeText(getContext(),
                                "Declined invitation for event " + notification.getEventID(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onViewEvent(Notification notification) {
                        Toast.makeText(getContext(),
                                "View event: " + notification.getEventID(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

        notificationList.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationList.setAdapter(notificationAdapter);

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

    /**
     * Loads the list of notifications for the current user based on device token.
     * Updates the RecyclerView adapter with the fetched notifications.
     */
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
