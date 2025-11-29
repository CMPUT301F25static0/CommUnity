package com.example.community.Screens.OrganizerScreens;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.community.DateValidation;
import com.example.community.EventService;
import com.example.community.LotteryService;
import com.example.community.R;
import com.example.community.UserService;

import java.time.LocalDate;
import java.util.Locale;

public class LotteryConfirmationDialogFragment extends DialogFragment {
    private static final String TAG = "LotteryConfirmationDialogFragment";
    private static final String ARG_EVENT_ID = "event_id";

    private String eventID;
    private int availableSlots;

    private EventService eventService;
    private LotteryService lotteryService;
    private UserService userService;

    private TextView lotteryMessageTextView;
    private NumberPicker lotteryNumberPicker;
    private ProgressBar lotteryLoadingProgressBar;
    private Button lotteryConfirmButton;
    private Button lotteryCancelButton;

    public static LotteryConfirmationDialogFragment newInstance(String eventId) {
        LotteryConfirmationDialogFragment fragment = new LotteryConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventService = new EventService();
        lotteryService = new LotteryService();
        userService = new UserService();

        if (getArguments() != null) {
            eventID = getArguments().getString(ARG_EVENT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View lotteryConfirmationDialog = inflater.inflate(R.layout.organizer_lottery_confirmation_dialog, container, false);
        return lotteryConfirmationDialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lotteryMessageTextView = view.findViewById(R.id.lotteryMessageTextView);
        lotteryNumberPicker = view.findViewById(R.id.lotteryNumberPicker);
        lotteryLoadingProgressBar = view.findViewById(R.id.lotteryLoadingProgressBar);
        lotteryConfirmButton = view.findViewById(R.id.lotteryConfirmButton);
        lotteryCancelButton = view.findViewById(R.id.lotteryCancelButton);

        eventService.getEvent(eventID)
                .addOnSuccessListener(event -> {
                    if (event != null) {
                        availableSlots = event.getMaxCapacity() - event.getCurrentCapacity();
                    } else {
                        availableSlots = 100;
                    }
                    setupNumberPicker(availableSlots);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get event data", e);
                    availableSlots = 100;
                    setupNumberPicker(availableSlots);
                });

//        setupNumberPicker(availableSlots);

        lotteryConfirmButton.setOnClickListener(v -> runLottery());
        lotteryCancelButton.setOnClickListener(v -> dismiss());
    }

    private void setupNumberPicker(int availableSlots) {
        lotteryNumberPicker.setMinValue(1);
        lotteryNumberPicker.setMaxValue(availableSlots);
        lotteryNumberPicker.setValue(1);
        lotteryNumberPicker.setWrapSelectorWheel(false);
    }

    private void runLottery() {
        int sampleSize = lotteryNumberPicker.getValue();

        // Get current organizer ID
        String deviceToken = userService.getDeviceToken();
        userService.getUserIDByDeviceToken(deviceToken)
                .addOnSuccessListener(organizerID -> {
                    // Get event to check registration end date
                    eventService.getEvent(eventID)
                            .addOnSuccessListener(event -> {
                                if (event == null) {
                                    Toast.makeText(getContext(), "Event not found", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Check if registration has closed
                                if (!isRegistrationClosed(event.getRegistrationEnd())) {
                                    Toast.makeText(getContext(), "Cannot run lottery: registration has not closed yet", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Show loading bar and start lottery
                                showLoading();
                                Toast.makeText(getContext(), "Running lottery...", Toast.LENGTH_SHORT).show();

                                lotteryService.runLottery(organizerID, eventID, sampleSize)
                                        .addOnSuccessListener(aVoid -> {
                                            hideLoading();
                                            Toast.makeText(getContext(), "Lottery finished successfully!", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                        })
                                        .addOnFailureListener(e -> {
                                            hideLoading();
                                            Log.e(TAG, "Lottery failed", e);
                                            Toast.makeText(getContext(), "Lottery failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to get event", e);
                                Toast.makeText(getContext(), "Failed to get event details", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get organizer ID", e);
                    Toast.makeText(getContext(), "Failed to verify organizer", Toast.LENGTH_SHORT).show();
                });
    }

    private boolean isRegistrationClosed(String registrationEndDate) {
        if (!DateValidation.isValidDateFormat(registrationEndDate)) {
            Log.e(TAG, "Invalid date format: " + registrationEndDate);
            return false;
        }
        try {
            LocalDate endDate = LocalDate.parse(registrationEndDate);
            LocalDate currentDate = LocalDate.now();
            return currentDate.isAfter(endDate);
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse registration end date", e);
            return false;
        }
    }

    private void showLoading() {
        lotteryMessageTextView.setText("Running lottery...");
        lotteryConfirmButton.setEnabled(false);
        lotteryCancelButton.setEnabled(false);
        lotteryLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        lotteryLoadingProgressBar.setVisibility(View.GONE);
        lotteryConfirmButton.setEnabled(true);
        lotteryCancelButton.setEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
        }
    }
}
