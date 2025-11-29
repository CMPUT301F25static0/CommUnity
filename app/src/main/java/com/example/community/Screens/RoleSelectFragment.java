package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;
import com.example.community.Role;
import com.example.community.UserService;
import com.google.firebase.auth.FirebaseAuth;

public class RoleSelectFragment extends Fragment {

    Button buttonUser, buttonHost, buttonAdmin;
    UserService userService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View roleSelectFragment = inflater.inflate(R.layout.role_select, container, false);
        return roleSelectFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonUser = view.findViewById(R.id.buttonUser);
        buttonHost = view.findViewById(R.id.buttonHost);
        buttonAdmin = view.findViewById(R.id.buttonAdmin);

        userService = new UserService();

        String deviceToken = userService.getDeviceToken();
//        userService.getUserIDByDeviceToken(deviceToken).getResult();

        buttonUser.setOnClickListener(v -> {
            if (deviceToken != null) {
                userService.getUserIDByDeviceToken(deviceToken)
                        .addOnSuccessListener(userId ->
                                userService.setRole(userId, Role.ENTRANT)
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(),
                                        "Failed to set role: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show()
                        );
            }

            NavHostFragment.findNavController(RoleSelectFragment.this)
                    .navigate(R.id.action_RoleSelectFragment_to_EntrantHomeFragment);
        });

        buttonHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userService
                        .getUserIDByDeviceToken(deviceToken)
                        .addOnSuccessListener(userId ->{userService.setRole(userId, Role.ORGANIZER)
                                .addOnSuccessListener(task ->{
                                    NavHostFragment.findNavController(RoleSelectFragment.this)
                                            .navigate(R.id.action_RoleSelectFragment_to_OrganizerHomeFragment);
                                });
                        });
            }
        });
//
        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userService
                        .getUserIDByDeviceToken(deviceToken)
                        .addOnSuccessListener(userId -> {userService.setRole(userId, Role.ADMIN)
                        .addOnSuccessListener(task -> {
                            NavHostFragment.findNavController(RoleSelectFragment.this)
                                    .navigate(R.id.action_RoleSelectFragment_to_AdminHomeFragment);});
                        });
            }
        });
    }
}
