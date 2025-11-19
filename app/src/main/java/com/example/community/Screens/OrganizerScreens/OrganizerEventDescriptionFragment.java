package com.example.community.Screens.OrganizerScreens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.community.Event;
import com.example.community.EventService;
import com.example.community.R;
import com.example.community.UserService;
import com.example.community.WaitingListEntryService;

public class OrganizerEventDescriptionFragment extends Fragment {

    public static final String TAG = "OrganizerEventDescriptionFragment";
    private static final String ARG_EVENT_ID = "event_id";

    private Event currentEvent;
    private WaitingListEntryService waitingListEntryService;
    private UserService userService;
    private EventService eventService;
    private String currentEntrantId;

    private TextView eventTitle, eventDescription, eventDates
            , registrationDates, capacity, waitlistCount, attendeeCount, invitedCount;
    private Button editButton, viewAttendeesButton, viewWaitlistButton, viewInvitedButton, backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View organizerEventDescriptionFragment = inflater.inflate(R.layout.organizer_event_description_page, container, false);
        return organizerEventDescriptionFragment;
    }
}
