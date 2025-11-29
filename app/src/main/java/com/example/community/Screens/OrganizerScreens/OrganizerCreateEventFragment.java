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

/**
 * Fragment for organizers to create or edit an event.
 * Allows input of event details including name, description, location, capacity,
 * waiting list, and start/end dates for both registration and the event itself.
 */
public class OrganizerCreateEventFragment extends Fragment {

    /** Tag used for logging */
    private final String TAG = "CreateEventFragment";

    /** Input field for the event name */
    private EditText eventNameInput;

    /** Input field for the event description */
    private EditText eventDescriptionInput;

    /** Input field for the event location */
    private EditText eventLocationInput;

    /** Input field for the maximum number of participants */
    private EditText eventMaxParticipantsInput;

    /** Input field for the waiting list size (optional) */
    private EditText waitingListSizeInput;

    /** Input field for the event start date */
    private EditText eventStartDateInput;

    /** Input field for the event end date */
    private EditText eventEndDateInput;

    /** Input field for registration start date */
    private EditText inputRegStart;

    /** Input field for registration end date */
    private EditText inputRegEnd;

    /** Button to cancel event creation/editing */
    private Button cancelButton;

    /** Button to submit the new or edited event */
    private Button submitButton;

    /** Service for event-related database operations */
    private EventService eventService;

    /** Service for user-related database operations */
    private UserService userService;

    /** Currently logged-in organizer */
    private User currentOrganizer;

    /** ID of the event being edited (null if creating a new event) */
    private String editingEventId;

    /** Flag indicating if the fragment is in editing mode */
    private boolean isEditing;

    /**
     * Inflates the fragment layout.
     *
     * @param inflater LayoutInflater to inflate views
     * @param container Parent view that fragment's UI should attach to
     * @param savedInstanceState Previously saved state of the fragment
     * @return Root View of the fragment layout
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.organizer_create_event_page, container, false);
    }

    /**
     * Initializes UI components, loads organizer data, sets up date pickers,
     * and sets click listeners for submit and cancel buttons.
     *
     * @param view Root view of the fragment
     * @param savedInstanceState Previously saved state of the fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventService = new EventService();
        userService = new UserService();

        eventNameInput = view.findViewById(R.id.inputEventName);
        eventDescriptionInput = view.findViewById(R.id.inputDescription);
        eventLocationInput = view.findViewById(R.id.inputEventLocation);
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

    /**
     * Loads current organizer's data from the UserService.
     * Navigates back if user data cannot be found or profile is incomplete.
     */
    private void loadOrganizerData() {
        String deviceToken = userService.getDeviceToken();

        userService.getByDeviceToken(deviceToken)
                .addOnSuccessListener(user -> {
                    if (user == null) {
                        Log.e(TAG, "User not found: " + deviceToken);
                        throw new IllegalArgumentException("User not found: " + deviceToken);
                    }
                    if (user.getUsername() == null || user.getUsername().isEmpty() ||
                            user.getEmail() == null || user.getEmail().isEmpty()) {
                        Toast.makeText(getContext(), "Please complete your profile first (username and email)", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                        return;
                    }
                    currentOrganizer = user;
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load Organizer data", e);
                    Toast.makeText(getContext(), "Failed to get user data", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this).navigateUp();
                });
    }

    /**
     * Loads existing event data into the form for editing.
     *
     * @param args Bundle containing the event's existing data
     */
    private void loadEventDataForEditing(Bundle args) {
        eventNameInput.setText(args.getString("event_name", ""));
        eventDescriptionInput.setText(args.getString("event_description", ""));
        eventLocationInput.setText(args.getString("event_location", ""));
        eventStartDateInput.setText(args.getString("event_start_date", ""));
        eventEndDateInput.setText(args.getString("event_end_date", ""));
        inputRegStart.setText(args.getString("reg_start", ""));
        inputRegEnd.setText(args.getString("reg_end", ""));
        eventMaxParticipantsInput.setText(String.valueOf(args.getInt("max_participants", 0)));
        int waitingListSize = args.getInt("waiting_list_size", 0);
        if (waitingListSize > 0) waitingListSizeInput.setText(String.valueOf(waitingListSize));
    }

    /**
     * Sets up click listeners on date input fields to open a DatePickerDialog.
     */
    private void setupDatePickers() {
        eventStartDateInput.setOnClickListener(v -> showDatePicker(eventStartDateInput));
        eventEndDateInput.setOnClickListener(v -> showDatePicker(eventEndDateInput));
        inputRegStart.setOnClickListener(v -> showDatePicker(inputRegStart));
        inputRegEnd.setOnClickListener(v -> showDatePicker(inputRegEnd));
    }

    /**
     * Creates a new event using the input fields.
     * Performs validation on all fields including dates and numeric values.
     * Generates a QR code for the event upon successful creation.
     */
    private void createEvent() {
        if (currentOrganizer == null) {
            Log.d(TAG, "createEvent: Organizer not found");
            Toast.makeText(getContext(), "Organizer not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String eventName = eventNameInput.getText().toString();
        String eventDescription = eventDescriptionInput.getText().toString();
        String eventLocation = eventLocationInput.getText().toString();
        String eventStartDate = eventStartDateInput.getText().toString();
        String eventEndDate = eventEndDateInput.getText().toString();
        String registrationStart = inputRegStart.getText().toString();
        String registrationEnd = inputRegEnd.getText().toString();

        if (eventName.isEmpty() || eventDescription.isEmpty() || eventLocation.isEmpty() ||
                eventStartDate.isEmpty() || eventEndDate.isEmpty() ||
                registrationStart.isEmpty() || registrationEnd.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int eventMaxParticipants;
        Integer waitingListSize = null;

        try {
            eventMaxParticipants = Integer.parseInt(eventMaxParticipantsInput.getText().toString().trim());
            if (eventMaxParticipants < 1) {
                Toast.makeText(getContext(), "Number of participants must be positive and greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid input for number of participants", Toast.LENGTH_SHORT).show();
            return;
        }

        String waitingListSizeStr = waitingListSizeInput.getText().toString().trim();
        if (!waitingListSizeStr.isEmpty()) {
            try {
                waitingListSize = Integer.parseInt(waitingListSizeStr);
                if (waitingListSize < 1) {
                    Toast.makeText(getContext(), "Waiting list size must be positive and greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid input for waiting list size", Toast.LENGTH_SHORT).show();
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

        if (!DateValidation.dateRangeValid(registrationEnd, eventStartDate)) {
            Toast.makeText(getContext(), "Registration must end before the event starts", Toast.LENGTH_SHORT).show();
            return;
        }

        eventService.createEvent(currentOrganizer.getUserID(), eventName, eventDescription, eventLocation,
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

    /**
     * Updates an existing event with values from input fields.
     * Performs validation and updates event in the database.
     */
    private void updateEvent() {
        if (currentOrganizer == null || editingEventId == null) {
            Toast.makeText(getContext(), "Error: Cannot update event", Toast.LENGTH_SHORT).show();
            return;
        }

        String eventName = eventNameInput.getText().toString();
        String eventDescription = eventDescriptionInput.getText().toString();
        String eventLocation = eventLocationInput.getText().toString();
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
        if (!DateValidation.dateRangeValid(registrationEnd, eventStartDate)) {
            Toast.makeText(getContext(), "Registration must end before the event starts", Toast.LENGTH_SHORT).show();
            return;
        }

        eventService.getEvent(editingEventId)
                .addOnSuccessListener(event -> {
                    event.setTitle(eventName);
                    event.setDescription(eventDescription);
                    event.setLocation(eventLocation);
                    event.setEventStartDate(eventStartDate);
                    event.setEventEndDate(eventEndDate);
                    event.setRegistrationStart(registrationStart);
                    event.setRegistrationEnd(registrationEnd);
                    event.setMaxCapacity(eventMaxParticipants);
                    if (waitingListSize != null) event.setWaitlistCapacity(waitingListSize);

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

    /**
     * Displays a DatePickerDialog for the given EditText input field.
     *
     * @param editText EditText to receive the selected date
     */
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
                initialDate.getMonthValue() - 1,
                initialDate.getDayOfMonth()
        );

        datePickerDialog.getDatePicker().setMinDate(minDate.toEpochDay());
        datePickerDialog.show();
    }
}
