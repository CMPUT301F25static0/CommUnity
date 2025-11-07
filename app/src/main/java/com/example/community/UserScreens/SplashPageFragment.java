package com.example.community.UserScreens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;
import com.example.community.UserService;

public class SplashPageFragment extends Fragment {

    private UserService userService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View splashFragment = inflater.inflate(R.layout.start_page, container, false);
        return splashFragment;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userService = new UserService();

        userService.splashScreenDeviceAuthentication()
                .addOnSuccessListener(user -> {
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_SplashPageFragment_to_RoleSelectFragment);
                });
    }


}
