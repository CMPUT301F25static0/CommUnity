package com.example.community.Screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.ArrayAdapters.EventArrayAdapter;
import com.example.community.Event;
import com.example.community.R;
import com.example.community.UserService;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserEventHistoryFragment extends Fragment {

    private RecyclerView waitlistedRecyclerView, joinedRecyclerView;
    private Button backButton;

    private ArrayList<Event> waitlistedEvents = new ArrayList<>();
    private ArrayList<Event> joinedEvents = new ArrayList<>();

    private EventArrayAdapter waitlistedAdapter;
    private EventArrayAdapter joinedAdapter;

    private FirebaseFirestore db;
    private UserService userService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_event_page, container, false);

        waitlistedRecyclerView = view.findViewById(R.id.recyclerWaitlistedEvents);
        joinedRecyclerView = view.findViewById(R.id.recyclerJoinedEvents);
        backButton = view.findViewById(R.id.back);

        // Setup RecyclerViews
        waitlistedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        joinedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        waitlistedAdapter = new EventArrayAdapter(waitlistedEvents);
        joinedAdapter = new EventArrayAdapter(joinedEvents);

        // Optional: click → go to event description
        joinedAdapter.setOnEventClickListener(event -> {
            Bundle args = new Bundle();
            args.putString("event_id", event.getEventID());
            NavHostFragment.findNavController(UserEventHistoryFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_EventDescriptionFragment, args);
        });

        waitlistedAdapter.setOnEventClickListener(event -> {
            Bundle args = new Bundle();
            args.putString("event_id", event.getEventID());
            NavHostFragment.findNavController(UserEventHistoryFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_EventDescriptionFragment, args);
        });

        waitlistedRecyclerView.setAdapter(waitlistedAdapter);
        joinedRecyclerView.setAdapter(joinedAdapter);

        backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(UserEventHistoryFragment.this).popBackStack()
        );

        db = FirebaseFirestore.getInstance();
        userService = new UserService();

        // Get the "CommUnity user ID" using the same pattern as RoleSelectFragment
        String deviceToken = userService.getDeviceToken();
        userService.getUserIDByDeviceToken(deviceToken)
                .addOnSuccessListener(userId -> {
                    Log.d("UserEventHistory", "Loaded userId = " + userId);
                    loadJoinedEvents(userId);
                    loadWaitlistedEvents(userId);
                })
                .addOnFailureListener(e ->
                        Log.e("UserEventHistory", "Failed to get userId from device token", e)
                );

        return view;
    }

    private void loadJoinedEvents(String userId) {
        db.collection("events")
                .whereArrayContains("attendeeListUserIDs", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    joinedEvents.clear();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Event e = doc.toObject(Event.class);
                        if (e != null) {
                            e.setEventID(doc.getId()); // ensure ID is set
                            joinedEvents.add(e);
                            Log.d("UserEventHistory", "Joined event: " + e.getTitle());
                        }
                    }
                    joinedAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Log.e("UserEventHistory", "Failed to load joined events", e)
                );
    }

    private void loadWaitlistedEvents(String userId) {
        db.collection("events")
                .whereArrayContains("waitListUserIDs", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    waitlistedEvents.clear();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Event e = doc.toObject(Event.class);
                        if (e != null) {
                            e.setEventID(doc.getId());
                            waitlistedEvents.add(e);
                            Log.d("UserEventHistory", "Waitlisted event: " + e.getTitle());
                        }
                    }
                    waitlistedAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Log.e("UserEventHistory", "Failed to load waitlisted events", e)
                );
    }
}

