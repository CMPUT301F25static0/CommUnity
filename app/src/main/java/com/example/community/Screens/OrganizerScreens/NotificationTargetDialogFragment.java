package com.example.community.Screens.OrganizerScreens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;
import com.example.community.Screens.NotificationsFragment;

public class NotificationTargetDialogFragment extends DialogFragment {

    private static final String TAG = "NotificationTargetDialogFragment";
    private static final String ARG_EVENT_ID = "event_id";

    private String eventID;

    private TextView messageTextView;
    private Button toWaitlistButton;
    private Button toInvitedButton;
    private Button toCancelledButton;

    public static NotificationTargetDialogFragment newInstance(String eventID) {
        NotificationTargetDialogFragment fragment = new NotificationTargetDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventID = getArguments().getString(ARG_EVENT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizer_notification_target_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageTextView = view.findViewById(R.id.notificationTargetMessage);
        toWaitlistButton = view.findViewById(R.id.waitlistNotificationButton);
        toInvitedButton = view.findViewById(R.id.invitedNotificationButton);
        toCancelledButton = view.findViewById(R.id.cancelledNotificationButton);

        toWaitlistButton.setOnClickListener(v ->
                navigateToCreateNotification("WAITLIST"));
        toInvitedButton.setOnClickListener(v ->
                navigateToCreateNotification("INVITED"));
        toCancelledButton.setOnClickListener(v ->
                navigateToCreateNotification("CANCELLED"));


    }

    private void navigateToCreateNotification(String entrantType) {
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventID);
        args.putString("entrant_type", entrantType);

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_OrganizerNotifyFragment_to_OrganizerCreateNotificationFragment, args);
        dismiss();
    }
}
