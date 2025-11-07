package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;

public class HostCreateEventFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.host_create_event_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);

        // Back to organizer home
        buttonCancel.setOnClickListener(v ->
                NavHostFragment.findNavController(HostCreateEventFragment.this).popBackStack());

        buttonSubmit.setOnClickListener(v ->
                NavHostFragment.findNavController(HostCreateEventFragment.this)
                        .navigate(R.id.action_HostCreateEventFragment_to_HostPosterUploadFragment)
        );
    }
}

