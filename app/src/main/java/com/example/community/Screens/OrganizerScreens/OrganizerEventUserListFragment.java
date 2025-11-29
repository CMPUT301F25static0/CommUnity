package com.example.community. Screens.OrganizerScreens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation. Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.ArrayAdapters.UserArrayAdapter;
import com.example.community.EventService;
import com.example.community.LotteryService;
import com.example.community.R;
import com.example.community. User;
import com.example.community.UserService;
import com.example.community.WaitingListEntry;
import com.example.community.WaitingListEntryService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util. List;
import java.util. Set;

public class OrganizerEventUserListFragment extends DialogFragment {

    public static final String TAG = "OrganizerEventUserListFragment";
    private static final String ARG_EVENT_ID = "event_id";
    private static final String ARG_LIST_TYPE = "list_type";

    private String eventId;
    private String listType;
    public List<User> usersList;
    private List<WaitingListEntry> waitingListEntries;
    private Set<String> selectedUserIds;

    private RecyclerView userListRecyclerView;
    private UserArrayAdapter userArrayAdapter;
    private TextView listTitle;
    private Button closeListButton;
    private Button cancelUsersButton;

    private WaitingListEntryService waitingListEntryService;
    private UserService userService;
    EventService eventService = new EventService();
    LotteryService lotteryService = new LotteryService();

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
        eventService = new EventService();
        lotteryService = new LotteryService();

        listTitle = view.findViewById(R. id.listTitle);
        userListRecyclerView = view. findViewById(R.id.userListRecyclerView);
        closeListButton = view.findViewById(R.id.closeListButton);
        cancelUsersButton = view. findViewById(R.id.cancelUsersButton);

        usersList = new ArrayList<>();
        waitingListEntries = new ArrayList<>();
        selectedUserIds = new HashSet<>();


        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_EVENT_ID);
            listType = getArguments().getString(ARG_LIST_TYPE);
        }

        userArrayAdapter = new UserArrayAdapter(usersList, listType);
        userArrayAdapter.setSelectionListener(((userId, selected) -> onUserSelectionChanged(userId, selected)));
        userListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        userListRecyclerView.setAdapter(userArrayAdapter);

        closeListButton.setOnClickListener(v -> dismiss());

        cancelUsersButton.setOnClickListener(v -> cancelSelectedUsers());



        loadUsersList();

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
                cancelUsersButton.setVisibility(View.GONE);
                loadWaitlistUsers();
                break;
            case "invited":
                listTitle. setText("Invited");
                cancelUsersButton.setVisibility(View.VISIBLE);
                loadInvitedUsers();
                break;
            case "attendees":
                listTitle.setText("Attendees");
                cancelUsersButton.setVisibility(View.GONE);
                loadAttendeesUsers();
                break;
            case "cancelled":
                listTitle.setText("Cancelled");
                cancelUsersButton.setVisibility(View. GONE);
                loadCancelledUsers();
                break;
            case "declined":
                listTitle.setText("Declined");
                cancelUsersButton.setVisibility(View. GONE);
                loadDeclinedUsers();
                break;
            default:
                Toast.makeText(getContext(), "List Type not valid", Toast.LENGTH_SHORT).show();
                dismiss();
        }
    }

    private void loadWaitlistUsers() {
        waitingListEntryService.getWaitlistEntries(eventId)
                .addOnSuccessListener(entries -> {
                    loadUsers(entries);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load waitlist entries", e);
                    dismiss();
                });
    }

    private void loadInvitedUsers() {
        waitingListEntryService.getInvitedList(eventId)
                .addOnSuccessListener(entries -> {
                    waitingListEntries.clear();
                    waitingListEntries.addAll(entries);
                    loadUsers(entries);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load waitlist entries", e);
                    dismiss();
                });
    }

    private void loadAttendeesUsers() {
        waitingListEntryService.getAcceptedList(eventId)
                .addOnSuccessListener(entries -> {
                    loadUsers(entries);
                })
                .addOnFailureListener(e -> {
                    Log. e(TAG, "Failed to load waitlist entries", e);
                    dismiss();
                });

    }

    private void loadCancelledUsers() {
        waitingListEntryService.getCancelledList(eventId)
                .addOnSuccessListener(entries -> {
                    loadUsers(entries);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load waitlist entries", e);
                    dismiss();
                });
    }

    private void loadDeclinedUsers() {
        waitingListEntryService.getDeclinedList(eventId)
                . addOnSuccessListener(entries -> {
                    waitingListEntries.clear();
                    waitingListEntries.addAll(entries);
                    loadUsers(entries);
                })
                . addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load waitlist entries", e);
                    dismiss();
                });
    }

    private void loadUsers(List<WaitingListEntry> entries) {
        usersList.clear();
        selectedUserIds.clear();

        if (entries. isEmpty()) {
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
                    . addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to load user", e);
                        loadedUsersCount[0]++;

                        if (loadedUsersCount[0] == entries.size()) {
                            userArrayAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    /**
     * Callback from adapter when user checkbox is toggled
     */
    public void onUserSelectionChanged(String userId, boolean selected) {
        if (selected) {
            selectedUserIds.add(userId);
        } else {
            selectedUserIds. remove(userId);
        }
    }

    /**
     * Cancels all selected users from the invited list
     */
    private void cancelSelectedUsers() {
        if (selectedUserIds.isEmpty()) {
            Toast.makeText(getContext(), "Please select at least one user", Toast. LENGTH_SHORT).show();
            return;
        }

        // Find the waitlist entries for the selected users
        List<WaitingListEntry> entriesToCancel = new ArrayList<>();
        for (WaitingListEntry entry : waitingListEntries) {
            if (selectedUserIds.contains(entry.getUserID())) {
                entriesToCancel.add(entry);
            }
        }

        // Cancel each entry
        int[] cancelledCount = {0};
        int totalToCancel = entriesToCancel.size();

        for (WaitingListEntry entry : entriesToCancel) {
            waitingListEntryService.cancelInvite(entry.getUserID(), eventId)
                    .addOnSuccessListener(v -> {
                        cancelledCount[0]++;
                        if (cancelledCount[0] == totalToCancel) {
                            Toast. makeText(getContext(), "Selected users cancelled successfully", Toast.LENGTH_SHORT).show();
                            runLotteryAgain(totalToCancel);
                            selectedUserIds.clear();
                            loadInvitedUsers();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to cancel user", e);
                        cancelledCount[0]++;
                        if (cancelledCount[0] == totalToCancel) {
                            Toast.makeText(getContext(), "Error cancelling some users", Toast.LENGTH_SHORT). show();
                            selectedUserIds.clear();
                            loadInvitedUsers();
                        }
                    });
        }
    }

    private void runLotteryAgain(int sampleSize) {
        eventService.getOrganizerID(eventId)
                .addOnSuccessListener(organizerID -> {
                    lotteryService.runLottery(organizerID, eventId, sampleSize)
                            .addOnSuccessListener(v -> {
                                Log.d(TAG, "Lottery ran successfully");
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to run lottery", e);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get organizer ID", e);
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set dialog size
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.75);
            getDialog().getWindow().setLayout(width, height);
        }
    }
}