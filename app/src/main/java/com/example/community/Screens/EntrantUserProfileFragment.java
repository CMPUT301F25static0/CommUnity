package com.example.community.Screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;
import com.example.community.User;
import com.example.community.UserService;

public class EntrantUserProfileFragment extends Fragment {
    EditText nameBox, emailBox, phoneBox;
    Button saveButton, deleteAccountButton;
    UserService userService;
    private User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View entrantUserProfileFragment = inflater.inflate(R.layout.entrant_profile, container, false);
        return entrantUserProfileFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameBox = view.findViewById(R.id.name_box);
        emailBox = view.findViewById(R.id.email_box);
        phoneBox = view.findViewById(R.id.phone_box);
        saveButton = view.findViewById(R.id.save_button);
        deleteAccountButton = view.findViewById(R.id.delete_unity);

        userService = new UserService();
        String deviceToken = userService.getDeviceToken();

        userService.getByDeviceToken(deviceToken)
                .addOnSuccessListener(user -> {
                    if (user == null) {
                        Log.e("EntrantUserProfileFragment", "User not found: " + deviceToken);
                        throw new IllegalArgumentException("User not found: " + deviceToken);
                    }
                    currentUser = user;
                    nameBox.setText(user.getUsername());
                    emailBox.setText(user.getEmail());
                    phoneBox.setText(user.getPhoneNumber());
                });

        saveButton.setOnClickListener(v -> {

            String newName = nameBox.getText().toString();
            String newEmail = emailBox.getText().toString();
            String newPhone = phoneBox.getText().toString();

            currentUser.setUsername(newName);
            currentUser.setEmail(newEmail);
            currentUser.setPhoneNumber(newPhone);

            userService.updateUser(currentUser);
            NavHostFragment.findNavController(EntrantUserProfileFragment.this).popBackStack();
        });



    }
}
