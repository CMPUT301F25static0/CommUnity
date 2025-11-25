package com.example.community.Screens.OrganizerScreens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.community.EventService;
import com.example.community.LotteryService;
import com.example.community.UserService;

public class LotteryConfirmationDialogFragment extends DialogFragment {
    private static final String TAG = "LotteryConfirmationDialogFragment";
    private static final String ARG_EVENT_ID = "event_id";

    private String eventID;

    private EventService eventService;
    private LotteryService lotteryService;
    private UserService userService;

    private TextView lotteryMessageTextView;
    private ProgressBar lotteryLoadingProgressBar;
    private Button lotteryConfirmButton;
    private Button lotteryCancelButton;

    public static LotteryConfirmationDialogFragment newInstance(String eventId) {
        LotteryConfirmationDialogFragment fragment = new LotteryConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventService = new EventService();
        lotteryService = new LotteryService();
        userService = new UserService();

        if (getArguments() != null) {
            eventID = getArguments().getString(ARG_EVENT_ID);
        }
    }


}
