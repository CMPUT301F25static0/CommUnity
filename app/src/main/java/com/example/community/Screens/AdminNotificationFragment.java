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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminNotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationArrayAdapter adapter;
    private List<Notification> notificationList;
    private FirebaseFirestore db;
    private Button backButton;

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
        adapter = new NotificationArrayAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        loadNotifications();

        backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminNotificationFragment.this).navigateUp();
        });
    }

    private void loadNotifications() {
        db.collection("notifications")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    notificationList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Notification notification = document.toObject(Notification.class);
                            notificationList.add(notification);
                        } catch (Exception e) {
                            Log.e("AdminNotification", "Error converting document", e);
                        }
                    }
                    adapter.notifyDataSetChanged();

                    if (notificationList.isEmpty()) {
                        Toast.makeText(getContext(), "No notifications found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("AdminNotification", "Error loading notifications", e);
                    Toast.makeText(getContext(), "Failed to load notifications", Toast.LENGTH_SHORT).show();
                });
    }
}
