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

import com.example.community.R;
import com.example.community.UserService;

/**
 * Home Fragment for Admin users.
 * Provides navigation to event management, host management,
 * profile settings, and image management screens.
 */
public class AdminHomeFragment extends Fragment {

    private Button buttonEvent, buttonHost, buttonProfile, buttonImage;
    private UserService userService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        buttonEvent = view.findViewById(R.id.buttonEvent);
        buttonHost = view.findViewById(R.id.buttonHost);
        buttonProfile = view.findViewById(R.id.buttonProfile);
        buttonImage = view.findViewById(R.id.buttonImage);

        userService = new UserService();

        setUpClickListeners();
    }

    /**
     * Sets up click listeners for admin navigation buttons.
     */
    private void setUpClickListeners() {
        buttonEvent.setOnClickListener(v ->
                NavHostFragment.findNavController(AdminHomeFragment.this)
                        .navigate(R.id.action_AdminHomeFragment_to_AdminEventFragment));

        buttonHost.setOnClickListener(v ->
                NavHostFragment.findNavController(AdminHomeFragment.this)
                        .navigate(R.id.action_AdminHomeFragment_to_AdminHostFragment));

        buttonProfile.setOnClickListener(v ->
                NavHostFragment.findNavController(AdminHomeFragment.this)
                        .navigate(R.id.action_AdminHomeFragment_to_AdminProfileFragment));

        buttonImage.setOnClickListener(v ->
                NavHostFragment.findNavController(AdminHomeFragment.this)
                        .navigate(R.id.action_AdminHomeFragment_to_AdminImageFragment));
    }
}
