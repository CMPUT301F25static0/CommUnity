package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

// ❗ Correct line:
import com.example.community.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Ignore;



/**
 * UI tests for Organizer user stories 02.xx.xx.
 *
 * All tests:
 *  - Launch MainActivity
 *  - Force navigation into the appropriate Organizer fragment via NavController
 *  - Use Espresso to interact with views defined in your XML files.
 */
@RunWith(AndroidJUnit4.class)
public class OrganizerUiTests {

    // ---------- Helpers to navigate into screens ---------- //

    /** Go to OrganizerHomeFragment from MainActivity. */
    private void navigateToOrganizerHome(ActivityScenario<MainActivity> scenario) {
        scenario.onActivity(activity -> {
            NavController navController = Navigation.findNavController(
                    activity, R.id.nav_host_fragment_activity_main);
            // Jump straight from Splash -> RoleSelect -> OrganizerHome
            navController.navigate(R.id.RoleSelectFragment);
            navController.navigate(R.id.OrganizerHomeFragment);
        });
    }

    /** Go directly to OrganizerCreateEventFragment. */
    private void navigateToCreateEvent(ActivityScenario<MainActivity> scenario) {
        scenario.onActivity(activity -> {
            NavController navController = Navigation.findNavController(
                    activity, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.RoleSelectFragment);
            navController.navigate(R.id.OrganizerHomeFragment);
            navController.navigate(R.id.CreateEventFragment);
        });
    }

    /** Go directly to OrganizerEventDescriptionFragment with a dummy event ID. */
    private void navigateToOrganizerEventDescription(ActivityScenario<MainActivity> scenario) {
        scenario.onActivity(activity -> {
            NavController navController = Navigation.findNavController(
                    activity, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.RoleSelectFragment);
            navController.navigate(R.id.OrganizerHomeFragment);

            Bundle args = new Bundle();
            args.putString("event_id", "testEventId");
            navController.navigate(R.id.OrganizerEventDescriptionFragment, args);
        });
    }

    /** Go directly to OrganizerPosterUploadFragment with a dummy event ID. */
    private void navigateToOrganizerPosterUpload(ActivityScenario<MainActivity> scenario) {
        scenario.onActivity(activity -> {
            NavController navController = Navigation.findNavController(
                    activity, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.RoleSelectFragment);
            navController.navigate(R.id.OrganizerHomeFragment);

            Bundle args = new Bundle();
            args.putString("event_id", "testEventId");
            navController.navigate(R.id.OrganizerPosterUploadFragment, args);
        });
    }

    /** Go directly to HostNotifyFragment. */
    private void navigateToHostNotify(ActivityScenario<MainActivity> scenario) {
        scenario.onActivity(activity -> {
            NavController navController = Navigation.findNavController(
                    activity, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.RoleSelectFragment);
            navController.navigate(R.id.OrganizerHomeFragment);
            navController.navigate(R.id.HostNotifyFragment);
        });
    }

    // ------------------------------------------------------
    // US 02.01.01 – Create event + QR (UI side of creating event)
    // ------------------------------------------------------

    /**
     * US 02.01.01
     * As an organizer I want to create a new event (UI flow).
     *
     * This verifies:
     *  - Organizer can see Create Event screen fields
     *  - Organizer can fill them
     *  - Organizer can press Submit button
     */
    @Test
    public void testUS020101_createEvent_basicFlow() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToCreateEvent(scenario);

        // Check main fields are visible
        onView(withId(R.id.inputEventName)).check(matches(isDisplayed()));
        onView(withId(R.id.inputDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.inputEventLocation)).check(matches(isDisplayed()));
        onView(withId(R.id.inputMaxParticipants)).check(matches(isDisplayed()));

        // Fill them
        onView(withId(R.id.inputEventName))
                .perform(replaceText("Test Event"), closeSoftKeyboard());
        onView(withId(R.id.inputDescription))
                .perform(replaceText("This is an event created in UI test."), closeSoftKeyboard());
        onView(withId(R.id.inputEventLocation))
                .perform(replaceText("Test Location"), closeSoftKeyboard());
        onView(withId(R.id.inputMaxParticipants))
                .perform(replaceText("50"), closeSoftKeyboard());

        // Click submit (we just ensure UI allows it; backend/QR generation logic is separate)
        onView(withId(R.id.buttonSubmit)).perform(click());
    }

    // ------------------------------------------------------
    // US 02.01.04 – Set registration period
    // ------------------------------------------------------

    /**
     * US 02.01.04
     * As an organizer, I want to set a registration period.
     *
     * This checks that organizer can fill registration start/end fields.
     */
    @Test
    public void testUS020104_setRegistrationPeriod() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToCreateEvent(scenario);

        onView(withId(R.id.inputRegistrationStart)).check(matches(isDisplayed()));
        onView(withId(R.id.inputRegistrationEnd)).check(matches(isDisplayed()));

        onView(withId(R.id.inputRegistrationStart))
                .perform(replaceText("2025-12-01"), closeSoftKeyboard());
        onView(withId(R.id.inputRegistrationEnd))
                .perform(replaceText("2025-12-10"), closeSoftKeyboard());

        // Submit to complete the flow
        onView(withId(R.id.buttonSubmit)).perform(click());
    }

    // ------------------------------------------------------
    // US 02.02.01 – View entrants on waiting list
    // ------------------------------------------------------

    /**
     * US 02.02.01
     * As an organizer I want to view the list of entrants who joined my event waiting list.
     *
     * This assumes:
     *  - OrganizerEventDescriptionFragment has a button viewWaitlistButton
     *  - Tapping it shows a dialog with RecyclerView userListRecyclerView
     */
    @Test
    public void testUS020201_viewWaitingList() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToOrganizerEventDescription(scenario);

        onView(withId(R.id.viewWaitlistButton)).check(matches(isDisplayed()));
        onView(withId(R.id.viewWaitlistButton)).perform(click());

        // Dialog should show RecyclerView
        onView(withId(R.id.userListRecyclerView)).check(matches(isDisplayed()));
    }

    // ------------------------------------------------------
    // US 02.02.02 – Map of where entrants joined (no obvious UI yet)
    // ------------------------------------------------------

    @Ignore("US 02.02.02 – No explicit map UI or view ID provided in XML yet.")
    @Test
    public void testUS020202_viewWaitlistOnMap() {
        // TODO: When you add a Map view or button for map, point this test to that ID
    }

    // ------------------------------------------------------
    // US 02.02.03 – Enable/disable geolocation requirement (no explicit UI yet)
    // ------------------------------------------------------

    @Ignore("US 02.02.03 – No toggle/switch UI for geolocation requirement in the current XML.")
    @Test
    public void testUS020203_toggleGeolocationRequirement() {
        // TODO: When you add a Switch/Checkbox etc in the organizer UI, wire this test to it.
    }

    // ------------------------------------------------------
    // US 02.03.01 – Optionally limit waiting list size
    // ------------------------------------------------------

    /**
     * US 02.03.01
     * As an organizer I want to OPTIONALLY limit the number of entrants
     * who can join my waiting list.
     *
     * This uses inputWaitingListSize in OrganizerCreateEventFragment.
     */
    @Test
    public void testUS020301_setOptionalWaitingListLimit() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToCreateEvent(scenario);

        onView(withId(R.id.inputWaitingListSize)).check(matches(isDisplayed()));
        onView(withId(R.id.inputWaitingListSize))
                .perform(replaceText("25"), closeSoftKeyboard());

        onView(withId(R.id.buttonSubmit)).perform(click());
    }

    // ------------------------------------------------------
    // US 02.04.01 – Upload event poster
    // ------------------------------------------------------

    /**
     * US 02.04.01
     * As an organizer I want to upload an event poster to the event details page.
     *
     * UI-level: we assert upload screen appears and upload button is tappable.
     */
    @Test
    public void testUS020401_openPosterUploadAndClickSelectImage() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToOrganizerPosterUpload(scenario);

        onView(withId(R.id.buttonUploadPoster)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonUploadPoster)).perform(click());

        // We can't actually open the gallery in this simple test,
        // but we know the button is working and visible.
    }

    // ------------------------------------------------------
    // US 02.04.02 – Update event poster
    // ------------------------------------------------------

    /**
     * US 02.04.02
     * As an organizer I want to update an event poster.
     *
     * UI-wise this is the same screen as upload; this test focuses on the
     * existence of preview + submit button.
     */
    @Test
    public void testUS020402_updatePosterPreviewAndSubmit() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToOrganizerPosterUpload(scenario);

        onView(withId(R.id.buttonUploadPoster)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonSubmitPoster)).check(matches(isDisplayed()));

        // Pretend user selected an image and button became enabled;
        // we at least assert it's clickable.
        onView(withId(R.id.buttonSubmitPoster)).perform(click());
    }

    // ------------------------------------------------------
    // US 02.05.01 – Send notification to chosen entrants
    // ------------------------------------------------------

    /**
     * US 02.05.01
     * As an organizer I want to send a notification to chosen entrants
     * to sign up for events.
     *
     * We treat "chosen entrants" message as inputNotifyAccepted in HostNotifyFragment.
     */
    @Test
    public void testUS020501_sendNotificationToChosenEntrants() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToHostNotify(scenario);

        onView(withId(R.id.inputNotifyAccepted)).check(matches(isDisplayed()));
        onView(withId(R.id.inputNotifyAccepted))
                .perform(replaceText("You have been selected!"), closeSoftKeyboard());

        onView(withId(R.id.buttonSend)).perform(click());
    }

    // ------------------------------------------------------
    // US 02.05.02 – Set sample size & run lottery
    // ------------------------------------------------------

    /**
     * US 02.05.02
     * As an organizer I want to set the system to sample a specified number
     * of attendees to register for the event.
     *
     * We use runLotteryButton from OrganizerEventDescriptionFragment
     * and ensure the lottery dialog appears.
     */
    @Test
    public void testUS020502_runLotteryShowsNumberPicker() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToOrganizerEventDescription(scenario);

        onView(withId(R.id.runLotteryButton)).check(matches(isDisplayed()));
        onView(withId(R.id.runLotteryButton)).perform(click());

        // Lottery dialog layout
        onView(withId(R.id.lotteryNumberPicker)).check(matches(isDisplayed()));
        onView(withId(R.id.lotteryConfirmButton)).check(matches(isDisplayed()));
        onView(withId(R.id.lotteryCancelButton)).check(matches(isDisplayed()));
    }

    // ------------------------------------------------------
    // US 02.05.03 – Draw replacement applicant
    // ------------------------------------------------------

    /**
     * US 02.05.03
     * As an organizer I want to draw a replacement applicant.
     *
     * At UI level this likely reuses the same lottery dialog;
     * We just verify the dialog can be opened again.
     */
    @Test
    public void testUS020503_reopenLotteryForReplacementApplicant() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToOrganizerEventDescription(scenario);

        // first run
        onView(withId(R.id.runLotteryButton)).perform(click());
        onView(withId(R.id.lotteryNumberPicker)).check(matches(isDisplayed()));
        onView(withId(R.id.lotteryCancelButton)).perform(click());

        // second run (replacement)
        onView(withId(R.id.runLotteryButton)).perform(click());
        onView(withId(R.id.lotteryNumberPicker)).check(matches(isDisplayed()));
    }

    // ------------------------------------------------------
    // US 02.06.01 – View all chosen entrants (invited list)
    // ------------------------------------------------------

    /**
     * US 02.06.01
     * As an organizer I want to view a list of all chosen entrants who are invited to apply.
     *
     * We use viewInvitedButton -> dialog with userListRecyclerView.
     */
    @Test
    public void testUS020601_viewInvitedEntrantsList() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToOrganizerEventDescription(scenario);

        onView(withId(R.id.viewInvitedButton)).check(matches(isDisplayed()));
        onView(withId(R.id.viewInvitedButton)).perform(click());

        onView(withId(R.id.userListRecyclerView)).check(matches(isDisplayed()));
    }

    // ------------------------------------------------------
    // US 02.06.02 – View cancelled entrants
    // ------------------------------------------------------

    /**
     * US 02.06.02
     * As an organizer I want to see a list of all the cancelled entrants.
     */
    @Test
    public void testUS020602_viewCancelledEntrantsList() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToOrganizerEventDescription(scenario);

        onView(withId(R.id.viewCancelledButton)).check(matches(isDisplayed()));
        onView(withId(R.id.viewCancelledButton)).perform(click());

        onView(withId(R.id.userListRecyclerView)).check(matches(isDisplayed()));
    }

    // ------------------------------------------------------
    // US 02.06.03 – Final list of entrants (attendees)
    // ------------------------------------------------------

    /**
     * US 02.06.03
     * As an organizer I want to see a final list of entrants who enrolled for the event.
     */
    @Test
    public void testUS020603_viewFinalAttendeeList() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToOrganizerEventDescription(scenario);

        onView(withId(R.id.viewAttendeesButton)).check(matches(isDisplayed()));
        onView(withId(R.id.viewAttendeesButton)).perform(click());

        onView(withId(R.id.userListRecyclerView)).check(matches(isDisplayed()));
    }

    // ------------------------------------------------------
    // US 02.06.04 – Cancel entrants who did not sign up (no explicit UI yet)
    // ------------------------------------------------------

    @Ignore("US 02.06.04 – No explicit 'cancel entrant' UI/button currently in XML.")
    @Test
    public void testUS020604_cancelNonSignupEntrants() {
        // TODO: Add a button or action for cancelling no-show entrants, then wire test.
    }

    // ------------------------------------------------------
    // US 02.06.05 – Export final list to CSV (no UI yet)
    // ------------------------------------------------------

    @Ignore("US 02.06.05 – No 'Export CSV' button or menu found in the current layouts.")
    @Test
    public void testUS020605_exportFinalListToCsv() {
        // TODO: When you add an 'Export to CSV' button, point this test to it and
        //       optionally check that a share/save intent is fired.
    }

    // ------------------------------------------------------
    // US 02.07.01 – Notifications to all waiting list entrants
    // ------------------------------------------------------

    /**
     * US 02.07.01
     * As an organizer I want to send notifications to all entrants on the waiting list.
     *
     * We treat inputNotifyWaiting + Send as that UI.
     */
    @Test
    public void testUS020701_sendNotificationsToWaitingList() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToHostNotify(scenario);

        onView(withId(R.id.inputNotifyWaiting)).check(matches(isDisplayed()));
        onView(withId(R.id.inputNotifyWaiting))
                .perform(replaceText("Message for waiting list"), closeSoftKeyboard());

        onView(withId(R.id.buttonSend)).perform(click());
    }

    // ------------------------------------------------------
    // US 02.07.02 – Notifications to selected entrants
    // ------------------------------------------------------

    /**
     * US 02.07.02
     * As an organizer I want to send notifications to all selected entrants.
     *
     * Again we use inputNotifyAccepted + Send.
     */
    @Test
    public void testUS020702_sendNotificationsToSelectedEntrants() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToHostNotify(scenario);

        onView(withId(R.id.inputNotifyAccepted)).check(matches(isDisplayed()));
        onView(withId(R.id.inputNotifyAccepted))
                .perform(replaceText("Message for selected entrants"), closeSoftKeyboard());

        onView(withId(R.id.buttonSend)).perform(click());
    }

    // ------------------------------------------------------
    // US 02.07.03 – Notifications to cancelled entrants
    // ------------------------------------------------------

    /**
     * US 02.07.03
     * As an organizer I want to send a notification to all cancelled entrants.
     *
     * We use inputNotifyCanceled + Send.
     */
    @Test
    public void testUS020703_sendNotificationsToCancelledEntrants() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        navigateToHostNotify(scenario);

        onView(withId(R.id.inputNotifyCanceled)).check(matches(isDisplayed()));
        onView(withId(R.id.inputNotifyCanceled))
                .perform(replaceText("Message for cancelled entrants"), closeSoftKeyboard());

        onView(withId(R.id.buttonSend)).perform(click());
    }