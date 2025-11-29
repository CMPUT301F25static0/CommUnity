package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.Event;
import com.example.community.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

    private EditText inputKeyword, inputTimeAvailable;
    private Button buttonApply, buttonBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputKeyword = view.findViewById(R.id.inputKeyword);
        inputTimeAvailable = view.findViewById(R.id.inputTimeAvailable);
        buttonApply = view.findViewById(R.id.buttonApplyFilter);
        buttonBack = view.findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(v ->
                NavHostFragment.findNavController(FilterFragment.this).popBackStack()
        );

        buttonApply.setOnClickListener(v -> {
            String keyword = inputKeyword.getText().toString().trim();
            String time = inputTimeAvailable.getText().toString().trim();

            if (keyword.isEmpty() && time.isEmpty()) {
                Toast.makeText(getContext(), "Enter a keyword or time to search", Toast.LENGTH_SHORT).show();
                return;
            }

            searchEvents(keyword, time);
        });
    }

    private void searchEvents(String keyword, String time) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("events");

        // Filter by title keyword
        if (!keyword.isEmpty()) {
            query = query.orderBy("title")
                    .startAt(keyword)
                    .endAt(keyword + "\uf8ff");
        }

        // Filter by time substring match
        // assuming eventStartDate or eventEndDate contains human-readable text (like "2025-11-26 17:00")
        if (!time.isEmpty()) {
            query = query.whereGreaterThanOrEqualTo("eventStartDate", time);
        }

        query.get().addOnSuccessListener(result -> {
            List<Event> matchingEvents = new ArrayList<>();
            for (QueryDocumentSnapshot doc : result) {
                Event event = doc.toObject(Event.class);
                matchingEvents.add(event);
            }

            Toast.makeText(getContext(),
                    "Found " + matchingEvents.size() + " matching events",
                    Toast.LENGTH_SHORT).show();

            // TODO: Pass matchingEvents to the event list UI (EntrantHomeFragment, etc.)
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(),
                        "Search failed: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show()
        );
    }
}

