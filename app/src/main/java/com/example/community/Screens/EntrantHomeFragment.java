package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.R;

public class EntrantHomeFragment extends Fragment {

    ImageButton entrantNotifications, entrantQRScanner;
    Button entrantFilterButton, eventHistoryButton, myProfileButton;
    RecyclerView entrantEventList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View entrantHomeFragment = inflater.inflate(R.layout.user_events, container, false);
        return entrantHomeFragment;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        entrantNotifications = view.findViewById(R.id.entrantNotifications);
        entrantQRScanner = view.findViewById(R.id.entrantQRScanner);
        entrantFilterButton = view.findViewById(R.id.filterButton);
        eventHistoryButton = view.findViewById(R.id.event_history);
        myProfileButton = view.findViewById(R.id.my_profile);
        entrantEventList = view.findViewById(R.id.event_list);


    }
}