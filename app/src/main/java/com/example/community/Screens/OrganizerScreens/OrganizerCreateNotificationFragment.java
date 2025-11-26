package com.example.community.Screens.OrganizerScreens;

import android.os.Bundle;
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

public class OrganizerCreateNotificationFragment extends Fragment {

    private static final String TAG = "CreateNotificationsFragment";

    private String eventID;
    private String entrantType;

    private TextView labelNotifyUsers;
    private EditText inputNotificationTitle;
    private EditText inputNotifyMessage;
    private Button buttonCancel;
    private Button buttonSend;

    private NotificationService notificationService;



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

        labelNotifyUsers = view.findViewById(R.id.labelNotifyUsers);
        inputNotificationTitle = view.findViewById(R.id.inputNotificationTitle);
        inputNotifyMessage = view.findViewById(R.id.inputNotifyMessage);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonSend = view.findViewById(R.id.buttonSend);

        if (getArguments() != null) {
            eventID = getArguments().getString("event_id");
            entrantType = getArguments().getString("entrant_type");
        }

        updateLabelBasedOnEntrantType();

        buttonCancel.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });

        buttonSend.setOnClickListener(v -> {
           Toast.makeText(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateLabelBasedOnEntrantType() {
        if (entrantType != null) {
            switch (entrantType) {
                case "WAITLIST":
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
}
