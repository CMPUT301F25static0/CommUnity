package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

import com.example.community.ArrayAdapters.EventArrayAdapter;
import com.example.community.Event;
import com.example.community.EventService;
import com.example.community.R;
import com.example.community.Role;
import com.example.community.UserService;

// delete user and remove event

public class AdminHomeFragment extends Fragment {

    Button buttonEvent, buttonHost, buttonProfile, buttonImage;

    UserService userService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_home_page, container, false);
    }

    @Override
    public void onViewCreated(@Nullable View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonEvent = view.findViewById(R.id.buttonEvent);
        buttonHost = view.findViewById(R.id.buttonHost);
        buttonProfile = view.findViewById(R.id.buttonProfile);
        buttonImage = view.findViewById(R.id.buttonImage);

        userService = new UserService();
        setUpClickListener();


    }

    private void setUpClickListener() {
        buttonEvent.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminHomeFragment.this)
                    .navigate(R.id.action_AdminHomeFragment_to_AdminEventFragment);
        });

        buttonHost.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminHomeFragment.this)
                    .navigate(R.id.action_AdminHomeFragment_to_AdminHostFragment);
        });

        buttonProfile.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminHomeFragment.this)
                    .navigate(R.id.action_AdminHomeFragment_to_AdminProfileFragment);
        });

        buttonImage.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminHomeFragment.this)
                    .navigate(R.id.action_AdminHomeFragment_to_AdminImageFragment);
        });
    }
}
