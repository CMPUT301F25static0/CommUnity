package com.example.community.Screens.OrganizerScreens;

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

import java.time.LocalDate;

public class OrganizerCreateEventFragment extends Fragment {

    private final String TAG = "CreateEventFragment";

    private EditText eventNameInput, eventDescriptionInput, hostNameInput;
    private EditText eventMaxParticipantsInput, waitingListSizeInput;
    private EditText eventStartDateInput, eventEndDateInput, inputRegStart, inputRegEnd;
    private Button cancelButton, submitButton;

    private EventService eventService;
    private UserService userService;
    private User currentOrganizer;

    private String editingEventId;
    private boolean isEditing;


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

        Bundle args = getArguments();
        if (args != null) {
            isEditing = args.getBoolean("is_edit_mode", false);
            if (isEditing) {
                editingEventId = args.getString("event_id");
                loadEventDataForEditing(args);
                submitButton.setText("Update Event");
                hostNameInput.setFocusable(false);
            }
        }

        loadOrganizerData();
        setupDatePickers();

        cancelButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        submitButton.setOnClickListener(v -> {
            if (isEditing) {
                updateEvent();
            } else {
                createEvent();
            }
        });

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

    private void loadEventDataForEditing(Bundle args) {
        String eventName = args.getString("event_name", "");
        String eventDescription = args.getString("event_description", "");
        String eventStartDate = args.getString("event_start_date", "");
        String eventEndDate = args.getString("event_end_date", "");
        String regStart = args.getString("reg_start", "");
        String regEnd = args.getString("reg_end", "");
        int maxParticipants = args.getInt("max_participants", 0);
        int waitingListSize = args.getInt("waiting_list_size", 0);

        eventNameInput.setText(eventName);
        eventDescriptionInput.setText(eventDescription);
        eventStartDateInput.setText(eventStartDate);
        eventEndDateInput.setText(eventEndDate);
        inputRegStart.setText(regStart);
        inputRegEnd.setText(regEnd);
        eventMaxParticipantsInput.setText(String.valueOf(maxParticipants));
        if (waitingListSize > 0) {
            waitingListSizeInput.setText(String.valueOf(waitingListSize));
        }
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

    private void updateEvent() {
        if (currentOrganizer == null || editingEventId == null) {
            Toast.makeText(getContext(), "Error: Cannot update event", Toast.LENGTH_SHORT).show();
            return;
        }

        String eventName = eventNameInput.getText().toString();
        String eventDescription = eventDescriptionInput.getText().toString();
        String eventStartDate = eventStartDateInput.getText().toString();
        String eventEndDate = eventEndDateInput.getText().toString();
        String registrationStart = inputRegStart.getText().toString();
        String registrationEnd = inputRegEnd.getText().toString();

        if (eventName.isEmpty() || eventDescription.isEmpty() ||
                eventStartDate.isEmpty() || eventEndDate.isEmpty() ||
                registrationStart.isEmpty() || registrationEnd.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int eventMaxParticipants;
//        Integer waitingListSize;

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

        String waitingListSizeStr = waitingListSizeInput.getText().toString().trim();
        Integer parsedWaitingListSize = null;
        if (!waitingListSizeStr.isEmpty()) {
            try {
                parsedWaitingListSize = Integer.parseInt(waitingListSizeStr);
                if (parsedWaitingListSize <= 0) {
                    Toast.makeText(getContext(), "Waiting list size must be positive", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid waiting list size", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        final Integer waitingListSize = parsedWaitingListSize;

        if (!DateValidation.dateRangeValid(registrationStart, registrationEnd)) {
            Toast.makeText(getContext(), "Invalid registration period", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!DateValidation.dateRangeValid(eventStartDate, eventEndDate)) {
            Toast.makeText(getContext(), "Invalid event period", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create updated event object
        eventService.getEvent(editingEventId)
                .addOnSuccessListener(event -> {
                    event.setTitle(eventName);
                    event.setDescription(eventDescription);
                    event.setEventStartDate(eventStartDate);
                    event.setEventEndDate(eventEndDate);
                    event.setRegistrationStart(registrationStart);
                    event.setRegistrationEnd(registrationEnd);
                    event.setMaxCapacity(eventMaxParticipants);
                    if (waitingListSize != null) {
                        event.setWaitlistCapacity(waitingListSize);
                    }

                    eventService.updateEvent(currentOrganizer.getUserID(), event)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Event updated successfully", Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(this).navigateUp();
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to update event", e);
                                Toast.makeText(getContext(), "Failed to update event", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to fetch event for update", e);
                    Toast.makeText(getContext(), "Failed to fetch event", Toast.LENGTH_SHORT).show();
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
