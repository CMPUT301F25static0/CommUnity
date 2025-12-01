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

/**
 * Fragment for filtering events based on a keyword or time availability.
 * Allows the user to enter search criteria and apply the filter.
 */
public class FilterFragment extends Fragment {

    private EditText inputKeyword, inputTimeAvailable;
    private Button buttonApply, buttonBack;

    /**
     * Inflates the filter fragment layout.
     *
     * @param inflater           LayoutInflater to inflate views
     * @param container          Parent view container
     * @param savedInstanceState Saved state bundle
     * @return Inflated fragment view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_page, container, false);
    }

    /**
     * Called after the fragment's view is created.
     * Initializes UI elements and sets up back and apply filter button listeners.
     *
     * @param view               The fragment's view
     * @param savedInstanceState Saved state bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputKeyword = view.findViewById(R.id.inputKeyword);
        inputTimeAvailable = view.findViewById(R.id.inputTimeAvailable);
        buttonApply = view.findViewById(R.id.buttonApplyFilter);
        buttonBack = view.findViewById(R.id.buttonBack);

        // Back button returns to previous screen
        buttonBack.setOnClickListener(v ->
                NavHostFragment.findNavController(FilterFragment.this).popBackStack()
        );

        // Apply button triggers search with entered criteria
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

    /**
     * Searches the Firestore "events" collection using the provided keyword and time filters.
     * Displays a Toast with the number of matching events found.
     *
     * @param keyword Keyword to search in event titles
     * @param time    Time string to filter events by start date
     */
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
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(),
                        "Search failed: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show()
        );
    }
}
