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

public class HostNotifyFragment extends Fragment {

    private EditText inputNotifyAccepted, inputNotifyWaiting, inputNotifyCanceled;
    private Button buttonCancel, buttonSend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.host_notify_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputNotifyAccepted = view.findViewById(R.id.inputNotifyAccepted);
        inputNotifyWaiting  = view.findViewById(R.id.inputNotifyWaiting);
        inputNotifyCanceled = view.findViewById(R.id.inputNotifyCanceled);
        buttonCancel        = view.findViewById(R.id.buttonCancel);
        buttonSend          = view.findViewById(R.id.buttonSend);

        buttonCancel.setOnClickListener(v ->
                NavHostFragment.findNavController(HostNotifyFragment.this).popBackStack()
        );

        buttonSend.setOnClickListener(v -> {
            // TODO: send notifications
            NavHostFragment.findNavController(HostNotifyFragment.this).popBackStack();
        });
    }
}

