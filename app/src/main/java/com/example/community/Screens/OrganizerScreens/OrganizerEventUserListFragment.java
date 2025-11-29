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

/**
 * A DialogFragment that displays a list of users associated with an event for organizers.
 * The fragment supports viewing lists of:
 * <ul>
 *     <li>Waitlist</li>
 *     <li>Invited</li>
 *     <li>Attendees</li>
 *     <li>Cancelled</li>
 *     <li>Declined</li>
 * </ul>
 */
public class OrganizerEventUserListFragment extends DialogFragment {

    /** Tag used for logging */
    public static final String TAG = "OrganizerEventUserListFragment";

    /** Argument key for the event ID */
    private static final String ARG_EVENT_ID = "event_id";

    /** Argument key for the type of list to display */
    private static final String ARG_LIST_TYPE = "list_type";

    /** ID of the event whose user list is displayed */
    private String eventId;

    /** Type of list to display: "waitlist", "invited", "attendees", "cancelled", "declined" */
    private String listType;

    /** List of users currently displayed */
    public List<User> usersList;

    /** RecyclerView and adapter for displaying the user list */
    private RecyclerView userListRecyclerView;
    private UserArrayAdapter userArrayAdapter;

    /** UI elements */
    private TextView listTitle;
    private Button closeListButton;

    /** Services for accessing waiting list entries and user data */
    private WaitingListEntryService waitingListEntryService;
    private UserService userService;

    /**
     * Creates a new instance of this fragment with the specified event ID and list type.
     *
     * @param eventId ID of the event
     * @param listType Type of user list ("waitlist", "invited", "attendees", "cancelled", "declined")
     * @return A new OrganizerEventUserListFragment instance
     */
    public static OrganizerEventUserListFragment newInstance(String eventId, String listType) {
        OrganizerEventUserListFragment fragment = new OrganizerEventUserListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        args.putString(ARG_LIST_TYPE, listType);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Inflates the fragment layout.
     *
     * @param inflater LayoutInflater used to inflate views
     * @param container Parent view for the fragment
     * @param savedInstanceState Previously saved state
     * @return Root view of the fragment layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.organizer_event_user_lists_dialog, container, false);
    }

    /**
     * Initializes UI components, sets up RecyclerView and adapter, loads event ID and list type,
     * and triggers loading of the user list.
     *
     * @param view Root view of the fragment
     * @param savedInstanceState Previously saved state
     */
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

        loadUsersList();
    }

    /**
     * Determines which type of user list to load and sets the list title.
     * Dismisses the fragment if parameters are invalid.
     */
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

    /** Loads the waitlist users for the event. */
    private void loadWaitlistUsers() {
        waitingListEntryService.getWaitlistEntries(eventId)
                .addOnSuccessListener(this::loadUsers)
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load waitlist entries", e);
                    dismiss();
                });
    }

    /** Loads the invited users for the event. */
    private void loadInvitedUsers() {
        waitingListEntryService.getInvitedList(eventId)
                .addOnSuccessListener(this::loadUsers)
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load invited entries", e);
                    dismiss();
                });
    }

    /** Loads the attendees users for the event. */
    private void loadAttendeesUsers() {
        waitingListEntryService.getAcceptedList(eventId)
                .addOnSuccessListener(this::loadUsers)
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load attendees entries", e);
                    dismiss();
                });
    }

    /** Loads the cancelled users for the event. */
    private void loadCancelledUsers() {
        waitingListEntryService.getCancelledList(eventId)
                .addOnSuccessListener(this::loadUsers)
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load cancelled entries", e);
                    dismiss();
                });
    }

    /** Loads the declined users for the event. */
    private void loadDeclinedUsers() {
        waitingListEntryService.getDeclinedList(eventId)
                .addOnSuccessListener(this::loadUsers)
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load declined entries", e);
                    dismiss();
                });
    }

    /**
     * Converts a list of WaitingListEntry objects into User objects and updates the RecyclerView.
     * Handles asynchronous loading and ensures adapter is updated once all users are loaded.
     *
     * @param entries List of waiting list entries
     */
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
