package com.example.community.Screens;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;
import com.example.community.UserService;

public class SplashPageFragment extends Fragment {

    private UserService userService;
    Button loginButton;
    Handler handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View splashFragment = inflater.inflate(R.layout.start_page, container, false);
        return splashFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userService = new UserService();
        loginButton = view.findViewById(R.id.loginButton);
        handler = new Handler(Looper.getMainLooper());

        loginButton.setOnClickListener(v -> {
            userService.splashScreenDeviceAuthentication()
                    .addOnSuccessListener(user -> {
                        NavHostFragment.findNavController(this)
                                .navigate(R.id.action_SplashPageFragment_to_RoleSelectFragment);
                     });
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                fadeIn.setDuration(500);

                loginButton.startAnimation(fadeIn);
                loginButton.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }


}
