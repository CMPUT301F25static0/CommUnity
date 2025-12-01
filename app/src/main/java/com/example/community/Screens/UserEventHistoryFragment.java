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

/**
 * Fragment displaying the event history of the current user.
 * Provides a simple back button to navigate to the previous screen.
 */
public class UserEventHistoryFragment extends Fragment {

    /**
     * Inflates the layout for the user's event history page.
     *
     * @param inflater           LayoutInflater used to inflate views
     * @param container          Parent container
     * @param savedInstanceState Saved instance state
     * @return The inflated view for this fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_event_page, container, false);
    }

    /**
     * Sets up UI elements and click listeners after the view is created.
     * Currently, only the back button is initialized to navigate up.
     *
     * @param view               The fragment's root view
     * @param savedInstanceState Saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button back = view.findViewById(R.id.back);
        back.setOnClickListener(v ->
                NavHostFragment.findNavController(UserEventHistoryFragment.this)
                        .popBackStack()
        );
    }
}
