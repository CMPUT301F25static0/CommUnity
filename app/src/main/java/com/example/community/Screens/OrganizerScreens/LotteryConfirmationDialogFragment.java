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

/**
 * DialogFragment that allows organizers to confirm and run a lottery
 * for an event, selecting a number of available slots.
 */
public class LotteryConfirmationDialogFragment extends DialogFragment {

    /** Tag for logging */
    private static final String TAG = "LotteryConfirmationDialogFragment";

    /** Argument key for event ID */
    private static final String ARG_EVENT_ID = "event_id";

    /** The ID of the event associated with this lottery */
    private String eventID;

    /** Number of available slots for the lottery */
    private int availableSlots;

    /** Service for retrieving event data */
    private EventService eventService;

    /** Service for running the lottery */
    private LotteryService lotteryService;

    /** Service for retrieving user/organizer info */
    private UserService userService;

    /** TextView displaying lottery messages to the user */
    private TextView lotteryMessageTextView;

    /** NumberPicker for selecting number of slots to include in the lottery */
    private NumberPicker lotteryNumberPicker;

    /** ProgressBar shown while the lottery is running */
    private ProgressBar lotteryLoadingProgressBar;

    /** Button to confirm and run the lottery */
    private Button lotteryConfirmButton;

    /** Button to cancel the lottery */
    private Button lotteryCancelButton;

    /**
     * Creates a new instance of the fragment with the given event ID.
     *
     * @param eventId The ID of the event for which the lottery will be run.
     * @return A new instance of LotteryConfirmationDialogFragment.
     */
    public static LotteryConfirmationDialogFragment newInstance(String eventId) {
        LotteryConfirmationDialogFragment fragment = new LotteryConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initializes services and retrieves arguments.
     *
     * @param savedInstanceState Previously saved state of the fragment.
     */
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

    /**
     * Inflates the fragment layout.
     *
     * @param inflater LayoutInflater to inflate the view.
     * @param container Parent view that fragment's UI should attach to.
     * @param savedInstanceState Previously saved state of the fragment.
     * @return Root View of the fragment layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.organizer_lottery_confirmation_dialog, container, false);
    }

    /**
     * Called after the view is created. Sets up UI references,
     * fetches event data, and sets button listeners.
     *
     * @param view Root view of the fragment.
     * @param savedInstanceState Previously saved state of the fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lotteryMessageTextView = view.findViewById(R.id.lotteryMessageTextView);
        lotteryNumberPicker = view.findViewById(R.id.lotteryNumberPicker);
        lotteryLoadingProgressBar = view.findViewById(R.id.lotteryLoadingProgressBar);
        lotteryConfirmButton = view.findViewById(R.id.lotteryConfirmButton);
        lotteryCancelButton = view.findViewById(R.id.lotteryCancelButton);

        // Fetch event to determine available slots
        eventService.getEvent(eventID)
                .addOnSuccessListener(event -> {
                    availableSlots = (event != null)
                            ? event.getMaxCapacity() - event.getCurrentCapacity()
                            : 100;
                    setupNumberPicker(availableSlots);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get event data", e);
                    availableSlots = 100;
                    setupNumberPicker(availableSlots);
                });

        lotteryConfirmButton.setOnClickListener(v -> runLottery());
        lotteryCancelButton.setOnClickListener(v -> dismiss());
    }

    /**
     * Configures the NumberPicker with minimum, maximum, and default values.
     *
     * @param availableSlots Maximum selectable value for the NumberPicker.
     */
    private void setupNumberPicker(int availableSlots) {
        lotteryNumberPicker.setMinValue(1);
        lotteryNumberPicker.setMaxValue(availableSlots);
        lotteryNumberPicker.setValue(1);
        lotteryNumberPicker.setWrapSelectorWheel(false);
    }

    /**
     * Executes the lottery process for the event.
     * Checks registration status, shows loading UI, and calls LotteryService.
     */
    private void runLottery() {
        int sampleSize = lotteryNumberPicker.getValue();

        String deviceToken = userService.getDeviceToken();
        userService.getUserIDByDeviceToken(deviceToken)
                .addOnSuccessListener(organizerID -> {
                    eventService.getEvent(eventID)
                            .addOnSuccessListener(event -> {
                                if (event == null) {
                                    Toast.makeText(getContext(), "Event not found", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (!isRegistrationClosed(event.getRegistrationEnd())) {
                                    Toast.makeText(getContext(), "Cannot run lottery: registration has not closed yet", Toast.LENGTH_SHORT).show();
                                    return;
                                }

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

    /**
     * Checks if the registration for the event has closed.
     *
     * @param registrationEndDate Registration end date as a string.
     * @return True if registration has ended, false otherwise.
     */
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

    /**
     * Updates the UI to indicate the lottery is running.
     */
    private void showLoading() {
        lotteryMessageTextView.setText("Running lottery...");
        lotteryConfirmButton.setEnabled(false);
        lotteryCancelButton.setEnabled(false);
        lotteryLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Updates the UI to indicate the lottery has finished or failed.
     */
    private void hideLoading() {
        lotteryLoadingProgressBar.setVisibility(View.GONE);
        lotteryConfirmButton.setEnabled(true);
        lotteryCancelButton.setEnabled(true);
    }

    /**
     * Ensures the dialog cannot be canceled by touching outside of it.
     */
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
        }
    }
}
