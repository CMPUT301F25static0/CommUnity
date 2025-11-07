package com.example.community.UserScreens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.community.R;

public class RoleSelectFragment extends Fragment {

    Button buttonUser, buttonHost, buttonAdmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View roleSelectFragment = inflater.inflate(R.layout.role_select, container, false);
        return roleSelectFragment;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        buttonUser = view.findViewById(R.id.buttonUser);
//        buttonHost = view.findViewById(R.id.buttonHost);
//        buttonAdmin = view.findViewById(R.id.buttonAdmin);
//
//        buttonUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        })
    }
}
