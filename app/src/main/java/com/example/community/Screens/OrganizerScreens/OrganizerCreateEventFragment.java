package com.example.community.Screens;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.DateValidation;
import com.example.community.EventService;
import com.example.community.R;
import com.example.community.User;
import com.example.community.UserService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventFragment extends Fragment {

    private final String TAG = "CreateEventFragment";

    private EditText eventNameInput, eventDescriptionInput, hostNameInput;
    private EditText eventMaxParticipantsInput, waitingListSizeInput;
    private EditText eventStartDateInput, eventEndDateInput, inputRegStart, inputRegEnd;
    private Button cancelButton, submitButton;

    private EventService eventService;
    private UserService userService;
    private User currentOrganizer;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View createEventFragment = inflater.inflate(R.layout.host_create_event_page, container, false);
        return createEventFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventService = new EventService();
        userService = new UserService();

        eventNameInput = view.findViewById(R.id.inputEventName);
        eventDescriptionInput = view.findViewById(R.id.inputDescription);
        hostNameInput = view.findViewById(R.id.inputHostName);
        eventMaxParticipantsInput = view.findViewById(R.id.inputMaxParticipants);
        waitingListSizeInput = view.findViewById(R.id.inputWaitingListSize);
        eventStartDateInput = view.findViewById(R.id.inputEventStart);
        eventEndDateInput = view.findViewById(R.id.inputEventEnd);
        inputRegStart = view.findViewById(R.id.inputRegistrationStart);
        inputRegEnd = view.findViewById(R.id.inputRegistrationEnd);
        cancelButton = view.findViewById(R.id.buttonCancel);
        submitButton = view.findViewById(R.id.buttonSubmit);

        eventStartDateInput.setFocusable(false);
        eventEndDateInput.setFocusable(false);
        inputRegStart.setFocusable(false);
        inputRegEnd.setFocusable(false);

        loadOrganizerData();
        setupDatePickers();

        cancelButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        submitButton.setOnClickListener(v -> createEvent());

    }

    private void loadOrganizerData() {
        String deviceToken = userService.getDeviceToken();

        userService.getByDeviceToken(deviceToken)
                .addOnSuccessListener(user -> {
                    if (user == null) {
                        Log.e(TAG, "User not found: " + deviceToken);
                        throw new IllegalArgumentException("User not found: " + deviceToken);
                    }
                    currentOrganizer = user;
                hostNameInput.setText(user.getUsername());
                });
    }

    private void setupDatePickers() {
        eventStartDateInput.setOnClickListener(v -> showDatePicker(eventStartDateInput));
        eventEndDateInput.setOnClickListener(v -> showDatePicker(eventEndDateInput));
        inputRegStart.setOnClickListener(v -> showDatePicker(inputRegStart));
        inputRegEnd.setOnClickListener(v -> showDatePicker(inputRegEnd));
    }

    private void createEvent() {
        if (currentOrganizer == null) {
            Log.d(TAG, "createEvent: Organizer not found");
            Toast.makeText(getContext(), "Organizer not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String eventName = eventNameInput.getText().toString();
        String eventDescription = eventDescriptionInput.getText().toString();
        String hostName = hostNameInput.getText().toString();
        String eventStartDate = eventStartDateInput.getText().toString();
        String eventEndDate = eventEndDateInput.getText().toString();
        String registrationStart = inputRegStart.getText().toString();
        String registrationEnd = inputRegEnd.getText().toString();

        if (eventName.isEmpty() || eventDescription.isEmpty() ||
                eventStartDate.toString().isEmpty() || eventEndDate.isEmpty() ||
                registrationStart.isEmpty() || registrationEnd.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int eventMaxParticipants;
        Integer waitingListSize = null;


        try {
            eventMaxParticipants = Integer.parseInt(eventMaxParticipantsInput.getText().toString().trim());
            if (eventMaxParticipants <= 0) {
                Toast.makeText(getContext(), "Number of participants must be positive", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid number of participants", Toast.LENGTH_SHORT).show();
            return;
        }

        // For optional waiting list size
        String waitingListSizeStr = waitingListSizeInput.getText().toString().trim();
        if (!waitingListSizeStr.isEmpty()) {
            try {
                waitingListSize = Integer.parseInt(waitingListSizeStr);
                if (waitingListSize <= 0) {
                    Toast.makeText(getContext(), "Waiting list size must be positive", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid waiting list size", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!DateValidation.dateRangeValid(registrationStart, registrationEnd)) {
            Toast.makeText(getContext(), "Invalid registration period", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!DateValidation.dateRangeValid(eventStartDate, eventEndDate)) {
            Toast.makeText(getContext(), "Invalid event period", Toast.LENGTH_SHORT).show();
            return;
        }

        eventService.createEvent(currentOrganizer.getUserID(), eventName, eventDescription,
                eventMaxParticipants, eventStartDate, eventEndDate, waitingListSize, registrationStart, registrationEnd)
                .addOnSuccessListener(eventId -> {
                    eventService.refreshEventQR(currentOrganizer.getUserID(), eventId)
                            .addOnSuccessListener(imageUrl -> {
                                Toast.makeText(getContext(), "Event created successfully with QR code", Toast.LENGTH_SHORT)
                                        .show();
                                NavHostFragment.findNavController(this).navigateUp();
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to generate QR code", e);
                                Toast.makeText(getContext(), "Event created but QR code generation failed", Toast.LENGTH_SHORT)
                                        .show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to create event", e);
                    Toast.makeText(getContext(), "Failed to create event", Toast.LENGTH_SHORT)
                            .show();
                });
    }
    private void showDatePicker(final EditText editText) {
        LocalDate minDate = LocalDate.now();
        LocalDate initialDate = LocalDate.now();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    editText.setText(selectedDate.format(DateValidation.DATE_FORMAT));

                },
                initialDate.getYear(),
                initialDate.getMonthValue() - 1 ,
                initialDate.getDayOfMonth()
        );
        datePickerDialog.getDatePicker().setMinDate(minDate.toEpochDay());
        datePickerDialog.show();
    }
}
