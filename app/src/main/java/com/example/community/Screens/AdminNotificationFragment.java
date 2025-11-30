package com.example.community.Screens;

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
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.ArrayAdapters.NotificationArrayAdapter;
import com.example.community.Notification;
import com.example.community.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdminNotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationArrayAdapter adapter;
    private List<Notification> notificationList;
    private FirebaseFirestore db;
    private Button backButton;

    private final Map<String, String> eventTitleMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_notification_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.adminNotificationView);
        backButton = view.findViewById(R.id.buttonBack);
        TextView headerTitle = view.findViewById(R.id.headerTitle);

        if (headerTitle != null) {
            headerTitle.setText("Notification Logs");
        }

        db = FirebaseFirestore.getInstance();
        notificationList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotificationArrayAdapter(notificationList, eventTitleMap);
        recyclerView.setAdapter(adapter);

        loadNotifications();

        backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminNotificationFragment.this).navigateUp();
        });
    }

    private void loadNotifications() {
        db.collection("notifications")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (getContext() == null) return;

                    notificationList.clear();
                    Set<String> uniqueEventIds = new HashSet<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Notification notification = document.toObject(Notification.class);
                            notificationList.add(notification);

                            if (notification.getEventID() != null && !notification.getEventID().isEmpty()) {
                                uniqueEventIds.add(notification.getEventID());
                            }
                        } catch (Exception e) {
                            Log.e("AdminNotification", "Error converting document", e);
                        }
                    }

                    if (notificationList.isEmpty()) {
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "No notifications found.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    fetchEventTitlesAndSort(uniqueEventIds);
                })
                .addOnFailureListener(e -> {
                    Log.e("AdminNotification", "Error loading notifications", e);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Failed to load notifications", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchEventTitlesAndSort(Set<String> eventIds) {
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

        for (String eid : eventIds) {
            tasks.add(db.collection("events").document(eid).get());
        }

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {
            if (getContext() == null) return;

            eventTitleMap.clear();

            for (Object obj : objects) {
                DocumentSnapshot snapshot = (DocumentSnapshot) obj;
                if (snapshot.exists()) {
                    String title = snapshot.getString("title");
                    eventTitleMap.put(snapshot.getId(), title != null ? title : "Unknown Event");
                }
            }

            sortNotifications();

            adapter.notifyDataSetChanged();

        }).addOnFailureListener(e -> {
            Log.e("AdminNotification", "Error fetching event details", e);
            sortNotifications();
            adapter.notifyDataSetChanged();
        });
    }

    private void sortNotifications() {
        Collections.sort(notificationList, (n1, n2) -> {
            long d1 = n1.getIssueDate();
            long d2 = n2.getIssueDate();
            int dateComp = Long.compare(d2, d1);
            if (dateComp != 0) return dateComp;

            String id1 = n1.getEventID() != null ? n1.getEventID() : "";
            String id2 = n2.getEventID() != null ? n2.getEventID() : "";

            String title1 = eventTitleMap.getOrDefault(id1, "General Notifications");
            String title2 = eventTitleMap.getOrDefault(id2, "General Notifications");

            int titleComp = title1.compareToIgnoreCase(title2);
            if (titleComp != 0) return titleComp;

            return id1.compareTo(id2);
        });
    }
}
