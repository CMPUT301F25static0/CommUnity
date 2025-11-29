package com.example.community.Screens.OrganizerScreens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;

/**
 * Fragment that allows the host to send notifications to users about
 * their accepted, waiting, or canceled status for an event.
 */
public class HostNotifyFragment extends Fragment {

    /** Input field for notification message to accepted users */
    private EditText inputNotifyAccepted;

    /** Input field for notification message to users on the waiting list */
    private EditText inputNotifyWaiting;

    /** Input field for notification message to users whose entries were canceled */
    private EditText inputNotifyCanceled;

    /** Button to cancel and go back without sending notifications */
    private Button buttonCancel;

    /** Button to send notifications */
    private Button buttonSend;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState Bundle containing the fragment's previously saved state.
     * @return The root View of the fragment's layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.host_notify_page, container, false);
    }

    /**
     * Called immediately after onCreateView() has returned.
     * Sets up references to input fields and buttons and their click listeners.
     *
     * @param view The View returned by onCreateView().
     * @param savedInstanceState Bundle containing the fragment's previously saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputNotifyAccepted = view.findViewById(R.id.inputNotifyAccepted);
        inputNotifyWaiting  = view.findViewById(R.id.inputNotifyWaiting);
        inputNotifyCanceled = view.findViewById(R.id.inputNotifyCanceled);
        buttonCancel        = view.findViewById(R.id.buttonCancel);
        buttonSend          = view.findViewById(R.id.buttonSend);

        // Go back without sending notifications
        buttonCancel.setOnClickListener(v ->
                NavHostFragment.findNavController(HostNotifyFragment.this).popBackStack()
        );

        // Send notifications and then go back
        buttonSend.setOnClickListener(v -> {
            // TODO: implement sending notifications to users
            NavHostFragment.findNavController(HostNotifyFragment.this).popBackStack();
        });
    }
}
