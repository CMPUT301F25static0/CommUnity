package com.example.community.Screens.OrganizerScreens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.ArrayAdapters.UserArrayAdapter;
import com.example.community.R;
import com.example.community.User;
import com.example.community.UserService;
import com.example.community.WaitingListEntry;
import com.example.community.WaitingListEntryService;

import java.util.ArrayList;
import java.util.List;

public class OrganizerEventUserListFragment extends DialogFragment {

    public static final String TAG = "OrganizerEventUserListFragment";
    private static final String ARG_EVENT_ID = "event_id";
    private static final String ARG_LIST_TYPE = "list_type";

    private String eventId;
    private String listType;
    public List<User> usersList;

    private RecyclerView userListRecyclerView;
    private UserArrayAdapter userArrayAdapter;
    private TextView listTitle;
    private Button closeListButton;

    private WaitingListEntryService waitingListEntryService;
    private UserService userService;

    public static OrganizerEventUserListFragment newInstance(String eventId, String listType) {
        OrganizerEventUserListFragment fragment = new OrganizerEventUserListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        args.putString(ARG_LIST_TYPE, listType);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizer_event_user_lists_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        waitingListEntryService = new WaitingListEntryService();
        userService = new UserService();

        listTitle = view.findViewById(R.id.listTitle);
        userListRecyclerView = view.findViewById(R.id.userListRecyclerView);
        closeListButton = view.findViewById(R.id.closeListButton);

        usersList = new ArrayList<>();
        userArrayAdapter = new UserArrayAdapter(usersList);
        userListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        userListRecyclerView.setAdapter(userArrayAdapter);

        closeListButton.setOnClickListener(v -> dismiss());

        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_EVENT_ID);
            listType = getArguments().getString(ARG_LIST_TYPE);
        }



    }

    private void loadUsersList() {
        if (eventId == null || listType == null) {
            Toast.makeText(getContext(), "Parameters received are invalid", Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        switch(listType) {
            case "waitlist":
                listTitle.setText("Waitlist");
                loadWaitlistUsers();
                break;
            case "invited":
                listTitle.setText("Invited");
                loadInvitedUsers();
                break;
            case "attendees":
                listTitle.setText("Attendees");
                loadAttendeesUsers();
                break;
            case "cancelled":
                listTitle.setText("Cancelled");
                loadCancelledUsers();
                break;
            case "declined":
                listTitle.setText("Declined");
                loadDeclinedUsers();
                break;
            default:
                Toast.makeText(getContext(), "List Type not valid", Toast.LENGTH_SHORT).show();
                dismiss();
        }
    }

    private void loadWaitlistUsers() {
     waitingListEntryService.getWaitlistEntries(eventId)
             .addOnSuccessListener(entries -> loadUsers(entries))
             .addOnFailureListener(e -> {
                 Log.e(TAG, "Failed to load waitlist entries", e);
                 dismiss();
             });
    }

    private void loadInvitedUsers() {
        waitingListEntryService.getInvitedList(eventId)
                .addOnSuccessListener(entries -> loadUsers(entries))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load waitlist entries", e);
                    dismiss();
                });
    }

    private void loadAttendeesUsers() {
        waitingListEntryService.getAcceptedList(eventId)
                .addOnSuccessListener(entries -> loadUsers(entries))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load waitlist entries", e);
                    dismiss();
                });

    }

    private void loadCancelledUsers() {
        waitingListEntryService.getCancelledList(eventId)
                .addOnSuccessListener(entries -> loadUsers(entries))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load waitlist entries", e);
                    dismiss();
                });
    }

    private void loadDeclinedUsers() {
        waitingListEntryService.getDeclinedList(eventId)
                .addOnSuccessListener(entries -> loadUsers(entries))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load waitlist entries", e);
                    dismiss();
                });
    }
    private void loadUsers(List<WaitingListEntry> entries) {
        usersList.clear();

        if (entries.isEmpty()) {
            userArrayAdapter.notifyDataSetChanged();
            return;
        }

        int[] loadedUsersCount = {0};

        for (WaitingListEntry entry : entries) {
            userService.getByUserID(entry.getUserID())
                    .addOnSuccessListener(user -> {
                        usersList.add(user);
                        loadedUsersCount[0]++;

                        if (loadedUsersCount[0] == entries.size()) {
                            userArrayAdapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to load user", e);
                        loadedUsersCount[0]++;

                        if (loadedUsersCount[0] == entries.size()) {
                            userArrayAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }
}

