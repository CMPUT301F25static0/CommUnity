package com.example.community.Screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;
import com.example.community.UserService;

public class DeleteAccountFragment extends DialogFragment {
    private static final String TAG = "DeleteAccountFragment";
    private UserService userService;
    private String userId;
    Button cancelDelete, confirmDelete;

    public static DeleteAccountFragment newInstance(String userId) {
        DeleteAccountFragment fragment = new DeleteAccountFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserService();
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.delete_unity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancelDelete = view.findViewById(R.id.cancel_popup);
        confirmDelete = view.findViewById(R.id.delete_unity);

        cancelDelete.setOnClickListener(v -> {
            dismiss();
        });

        confirmDelete.setOnClickListener(v -> {
            userService.deleteUser(userId)
                    .addOnSuccessListener(task -> {
                        dismiss();
                        Log.d(TAG, "onViewCreated: Account deleted successfully");
                        Toast.makeText(getActivity(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                        NavHostFragment.findNavController(this)
                                .navigate(R.id.action_EntrantUserProfileFragment_to_SplashPageFragment);
                    })
                    .addOnFailureListener(e -> {
                        dismiss();
                        Log.d(TAG, "onViewCreated: Failed to delete account");
                        Toast.makeText(getActivity(), "Failed to delete account", Toast.LENGTH_SHORT).show();
                    });
        });
    }

}
