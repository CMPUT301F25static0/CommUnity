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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.R;
import com.example.community.User;
import com.example.community.UserService;

import java.util.ArrayList;
import java.util.List;

    public class AdminHostFragment extends Fragment implements com.example.community.ArrayAdapters.AdminHostAdapter.OnHostListener {

        Button backButton;

        private static final String TAG = "AdminHostFragment";

        private RecyclerView recyclerView;
        private com.example.community.ArrayAdapters.AdminHostAdapter adapter;
        private List<User> userList;
        private UserService userService;
        private NavController navController;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            // Inflate the fragment's layout
            return inflater.inflate(R.layout.admin_host_page, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Initialize NavController, UserService, and user list
            navController = Navigation.findNavController(view);
            backButton = view.findViewById(R.id.buttonBack);
            userService = new UserService();
            userList = new ArrayList<>();

            // Setup RecyclerView
            recyclerView = view.findViewById(R.id.adminHostView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new com.example.community.ArrayAdapters.AdminHostAdapter(getContext(), userList, this);
            recyclerView.setAdapter(adapter);

            getParentFragmentManager().setFragmentResultListener(
                    DeleteAccountConfirmDialogFragment.REQUEST_KEY,
                    this.getViewLifecycleOwner(),
                    (requestKey, result) -> {
                        if (result.getBoolean(DeleteAccountConfirmDialogFragment.RESULT_CONFIRMED)) {

                            // 1. Retrieve the data we stored in the dialog's arguments earlier
                            Fragment dialogFragment = getParentFragmentManager().findFragmentByTag("DeleteAccountConfirmDialog");
                            if (dialogFragment != null && dialogFragment.getArguments() != null) {
                                String userID = dialogFragment.getArguments().getString("userID");
                                int position = dialogFragment.getArguments().getInt("position");

                                // 2. Perform the actual deletion
                                performDeleteUser(userID, position);
                            }
                        }
                    }
            );

            // Fetch user data
            loadUsers();
            setUpClickListener();
        }

        private void performDeleteUser(String userID, int position) {
            userService.deleteUser(userID).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User deleted successfully: " + userID);

                    if (position < userList.size()) {
                        userList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, userList.size());
                    }
                    Toast.makeText(getContext(), "User deleted.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Failed to delete user", task.getException());
                    Toast.makeText(getContext(), "Failed to delete user.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void loadUsers() {
            userService.getAllUsers().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userList.clear();
                    userList.addAll(task.getResult());
                    adapter.notifyDataSetChanged(); // Refresh the list in the UI
                    Log.d(TAG, "Successfully loaded " + userList.size() + " users.");
                } else {
                    Log.e(TAG, "Failed to load users", task.getException());
                    Toast.makeText(getContext(), "Failed to load users.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onViewClicked(User user) {
            // Create a bundle to pass the data
            Bundle bundle = new Bundle();
            bundle.putString("userID", user.getUserID());

            // Navigate using the ID directly, passing the bundle
            navController.navigate(R.id.action_AdminHostFragment_to_EntrantUserProfileFragment, bundle);
        }


        @Override
        public void onDeleteClicked(User user, int position) {
            DeleteAccountConfirmDialogFragment dialog = new DeleteAccountConfirmDialogFragment();

            // Pass the User ID and Position to the dialog so we know WHO to delete later
            Bundle args = new Bundle();
            args.putString("userID", user.getUserID());
            args.putInt("position", position);
            dialog.setArguments(args);

            dialog.show(getParentFragmentManager(), "DeleteAccountConfirmDialog");
        }


        private void setUpClickListener() {
            backButton.setOnClickListener(v -> {
                NavHostFragment.findNavController(AdminHostFragment.this)
                        .navigate(R.id.action_AdminHostFragment_to_AdminHomeFragment);
            });
        }
    }
