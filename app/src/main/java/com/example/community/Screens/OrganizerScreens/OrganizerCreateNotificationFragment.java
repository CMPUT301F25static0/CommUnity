package com.example.community.Screens.OrganizerScreens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.NotificationService;
import com.example.community.R;
import com.example.community.User;
import com.example.community.UserService;

public class OrganizerCreateNotificationFragment extends Fragment {

    private static final String TAG = "CreateNotificationsFragment";

    private String eventID;
    private String entrantType;
    private User currentOrganizer;

    private TextView labelNotifyUsers;
    private EditText inputNotificationTitle;
    private EditText inputNotificationMessage;
    private Button buttonCancel;
    private Button buttonSend;

    private NotificationService notificationService;
    private UserService userService;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizer_create_notification_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notificationService = new NotificationService();
        userService = new UserService();

        labelNotifyUsers = view.findViewById(R.id.labelNotifyUsers);
        inputNotificationTitle = view.findViewById(R.id.inputNotificationTitle);
        inputNotificationMessage = view.findViewById(R.id.inputNotifyMessage);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonSend = view.findViewById(R.id.buttonSend);

        if (getArguments() != null) {
            eventID = getArguments().getString("event_id");
            entrantType = getArguments().getString("entrant_type");
        }

        loadOrganizerData();
        updateLabelBasedOnEntrantType();

        buttonCancel.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });

        buttonSend.setOnClickListener(v -> {
           sendNotifications();
        });
    }

    private void loadOrganizerData() {
        String deviceToken = userService.getDeviceToken();

        userService.getByDeviceToken(deviceToken)
                .addOnSuccessListener(user -> {
                    if (user == null) {
                        Log.e(TAG, "User not found: " + deviceToken);
                        throw new IllegalArgumentException("User not found: " + deviceToken);
                    }
                    if (user.getUsername() == null || user.getUsername().isEmpty() ||
                            user.getEmail() == null || user.getEmail().isEmpty()) {
                        Toast.makeText(getContext(), "Please complete your profile first (username and email)", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                        return;
                    }
                    currentOrganizer = user;
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load Organizer data", e);
                    Toast.makeText(getContext(), "Failed to get user data", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this).navigateUp();
                });
    }

    private void updateLabelBasedOnEntrantType() {
        if (entrantType != null) {
            switch (entrantType) {
                case "WAITING":
                    labelNotifyUsers. setText("Notify Waiting List Entrants");
                    break;
                case "INVITED":
                    labelNotifyUsers.setText("Notify Invited Entrants");
                    break;
                case "CANCELLED":
                    labelNotifyUsers.setText("Notify Cancelled Entrants");
                    break;
                default:
                    labelNotifyUsers.setText("Notify Users");
            }
        }
    }

    private void sendNotifications() {
        String title = inputNotificationTitle.getText().toString().trim();
        String message = inputNotificationMessage.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a notification title", Toast.LENGTH_SHORT);
            return;
        }
        if (message.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a notification message", Toast.LENGTH_SHORT);
            return;
        }
        if (eventID == null || eventID.isEmpty()) {
            Toast.makeText(getContext(), "EventID is missing", Toast.LENGTH_SHORT);
            return;
        }
        if (entrantType == null || entrantType.isEmpty()) {
            Toast.makeText(getContext(), "Entrant type is missing", Toast.LENGTH_SHORT);
            return;
        }

        sendNotificationByType(title, message);
    }

    private void sendNotificationByType(String title, String message) {
        Toast.makeText(getContext(), "Sending notification...", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "=== sendNotificationByType START ===");
        Log.d(TAG, "Title: " + title);
        Log. d(TAG, "Message: " + message);
        Log. d(TAG, "EventID: " + eventID);
        Log.d(TAG, "EntrantType: " + entrantType);
        Log.d(TAG, "Organizer ID: " + (currentOrganizer != null ? currentOrganizer.getUserID() : "NULL"));

        switch (entrantType) {
            case "WAITING":
                Log.d(TAG, ">>> Calling broadcastToWaitlist");
                notificationService.broadcastToWaitlist(currentOrganizer.getUserID(), eventID, title, message)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "✓ broadcastToWaitlist SUCCESS");
                            Toast.makeText(getContext(), "Notification sent to waiting list!", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(OrganizerCreateNotificationFragment. this).navigateUp();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "✗ broadcastToWaitlist FAILED", e);
                            Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast. LENGTH_SHORT).show();
                        });
                break;

            case "INVITED":
                // Send to invited entrants
                notificationService.broadcastToInvited(currentOrganizer.getUserID(), eventID, title, message)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Notification sent to invited entrants!", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(OrganizerCreateNotificationFragment.this). navigateUp();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Failed to send notification", e);
                            Toast.makeText(getContext(), "Failed to send notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                break;

            case "CANCELLED":
                // Send to cancelled entrants
                notificationService.broadcastToCancelled(currentOrganizer.getUserID(), eventID, title, message)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Notification sent to cancelled entrants!", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(OrganizerCreateNotificationFragment.this).navigateUp();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Failed to send notification", e);
                            Toast. makeText(getContext(), "Failed to send notification: " + e. getMessage(), Toast.LENGTH_SHORT).show();
                        });
                break;
            default:
                Log.e(TAG, "UNKNOWN ENTRANT TYPE: " + entrantType);
        }
    }


}
