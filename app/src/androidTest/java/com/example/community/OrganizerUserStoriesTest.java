package com.example.community;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.community.Screens.OrganizerScreens.LotteryConfirmationDialogFragment;
import com.example.community.Screens.OrganizerScreens.OrganizerCreateEventFragment;
import com.example.community.Screens.OrganizerScreens.OrganizerCreateNotificationFragment;
import com.example.community.Screens.OrganizerScreens.OrganizerEventDescriptionFragment;
import com.example.community.Screens.OrganizerScreens.OrganizerEventUserListFragment;
import com.example.community.Screens.OrganizerScreens.OrganizerGeolocationMapFragment;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganizerUserStoriesTest {

    private static final String DUMMY_EVENT_ID = "dummy-event-id";

    private Bundle argsForEventDescription() {
        Bundle args = new Bundle();
        args.putString("event_id", DUMMY_EVENT_ID);
        return args;
    }

    private Bundle argsForUserList(String listType) {
        Bundle args = new Bundle();
        args.putString("event_id", DUMMY_EVENT_ID);
        args.putString("list_type", listType);
        return args;
    }

    private Bundle argsForNotification(String entrantType) {
        Bundle args = new Bundle();
        args.putString("event_id", DUMMY_EVENT_ID);
        args.putString("entrant_type", entrantType);
        return args;
    }

    private Bundle argsForMap() {
        Bundle args = new Bundle();
        args.putString("event_id", DUMMY_EVENT_ID);
        return args;
    }

    private Bundle argsForLotteryDialog() {
        Bundle args = new Bundle();
        args.putString("event_id", DUMMY_EVENT_ID);
        return args;
    }

    // ---------------------------------------------------------
    // US 02.01.01 – create event & poster/QR-related details
    // ---------------------------------------------------------
    @Test
    public void test_US_02_01_01() {
        FragmentScenario.launchInContainer(OrganizerCreateEventFragment.class);

        onView(withId(R.id.inputEventName)).check(matches(isDisplayed()));
        onView(withId(R.id.inputDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.inputEventLocation)).check(matches(isDisplayed()));
        onView(withId(R.id.inputEventStart)).check(matches(isDisplayed()));
        onView(withId(R.id.inputEventEnd)).check(matches(isDisplayed()));
        onView(withId(R.id.inputRegistrationStart)).check(matches(isDisplayed()));
        onView(withId(R.id.inputRegistrationEnd)).check(matches(isDisplayed()));

        FragmentScenario.launchInContainer(
                OrganizerEventDescriptionFragment.class,
                argsForEventDescription()
        );
        onView(withId(R.id.eventTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPosterButton)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.01.04 – registration period fields
    // ---------------------------------------------------------
    @Test
    public void test_US_02_01_04() {
        FragmentScenario.launchInContainer(OrganizerCreateEventFragment.class);

        onView(withId(R.id.inputRegistrationStart)).check(matches(isDisplayed()));
        onView(withId(R.id.inputRegistrationEnd)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.02.01 – view waiting list button
    // ---------------------------------------------------------
    @Test
    public void test_US_02_02_01() {
        FragmentScenario.launchInContainer(
                OrganizerEventDescriptionFragment.class,
                argsForEventDescription()
        );

        onView(withId(R.id.viewWaitlistButton)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.02.02 – map of where entrants joined
    // ---------------------------------------------------------
    @Test
    public void test_US_02_02_02() {
        FragmentScenario.launchInContainer(
                OrganizerGeolocationMapFragment.class,
                argsForMap()
        );

        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.02.03 – toggle geolocation requirement
    // ---------------------------------------------------------
    @Test
    public void test_US_02_02_03() {
        FragmentScenario.launchInContainer(OrganizerCreateEventFragment.class);

        onView(withId(R.id.checkboxGeolocationRequired))
                .perform(scrollTo(), click());
        onView(withId(R.id.checkboxGeolocationRequired))
                .check(matches(isChecked()));

        onView(withId(R.id.checkboxGeolocationRequired))
                .perform(scrollTo(), click());
        onView(withId(R.id.checkboxGeolocationRequired))
                .check(matches(isNotChecked()));
    }

    // ---------------------------------------------------------
    // US 02.03.01 – optional waiting list size
    // ---------------------------------------------------------
    @Test
    public void test_US_02_03_01() {
        FragmentScenario.launchInContainer(OrganizerCreateEventFragment.class);

        // Clear (no limit)
        onView(withId(R.id.inputWaitingListSize))
                .perform(scrollTo(), clearText(), closeSoftKeyboard());
        // Set a limit
        onView(withId(R.id.inputWaitingListSize))
                .perform(scrollTo(), replaceText("10"), closeSoftKeyboard());
    }

    // ---------------------------------------------------------
    // US 02.04.01 – upload poster button exists
    // ---------------------------------------------------------
    @Test
    public void test_US_02_04_01() {
        FragmentScenario.launchInContainer(
                OrganizerEventDescriptionFragment.class,
                argsForEventDescription()
        );

        onView(withId(R.id.uploadPosterButton)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.04.02 – update poster uses same control
    // ---------------------------------------------------------
    @Test
    public void test_US_02_04_02() {
        FragmentScenario.launchInContainer(
                OrganizerEventDescriptionFragment.class,
                argsForEventDescription()
        );

        onView(withId(R.id.uploadPosterButton)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.05.01 – notification screen for invited entrants
    // ---------------------------------------------------------
    @Test
    public void test_US_02_05_01() {
        FragmentScenario.launchInContainer(
                OrganizerCreateNotificationFragment.class,
                argsForNotification("INVITED")
        );

        onView(withId(R.id.inputNotificationTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.inputNotifyMessage)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.05.02 – lottery sampling dialog (number picker)
    // ---------------------------------------------------------
    @Test
    public void test_US_02_05_02() {
        FragmentScenario.launchInContainer(
                LotteryConfirmationDialogFragment.class,
                argsForLotteryDialog()
        );

        onView(withId(R.id.lotteryNumberPicker)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.05.03 – replacement draw (same dialog)
    // ---------------------------------------------------------
    @Test
    public void test_US_02_05_03() {
        FragmentScenario.launchInContainer(
                LotteryConfirmationDialogFragment.class,
                argsForLotteryDialog()
        );

        onView(withId(R.id.lotteryNumberPicker)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.06.01 – invited list button
    // ---------------------------------------------------------
    @Test
    public void test_US_02_06_01() {
        FragmentScenario.launchInContainer(
                OrganizerEventDescriptionFragment.class,
                argsForEventDescription()
        );

        onView(withId(R.id.viewInvitedButton)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.06.02 – cancelled list button
    // ---------------------------------------------------------
    @Test
    public void test_US_02_06_02() {
        FragmentScenario.launchInContainer(
                OrganizerEventDescriptionFragment.class,
                argsForEventDescription()
        );

        onView(withId(R.id.viewCancelledButton)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.06.03 – attendees list button
    // ---------------------------------------------------------
    @Test
    public void test_US_02_06_03() {
        FragmentScenario.launchInContainer(
                OrganizerEventDescriptionFragment.class,
                argsForEventDescription()
        );

        onView(withId(R.id.viewAttendeesButton)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.06.04 – cancel users list UI
    // ---------------------------------------------------------
    @Test
    public void test_US_02_06_04() {
        FragmentScenario.launchInContainer(
                OrganizerEventUserListFragment.class,
                argsForUserList("INVITED")
        );

        onView(withId(R.id.userListRecyclerView)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.06.05 – export CSV button
    // ---------------------------------------------------------
    @Test
    public void test_US_02_06_05() {
        FragmentScenario.launchInContainer(
                OrganizerEventDescriptionFragment.class,
                argsForEventDescription()
        );

        onView(withId(R.id.exportAttendeesButton)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.07.01 – notify waiting list entrants
    // ---------------------------------------------------------
    @Test
    public void test_US_02_07_01() {
        FragmentScenario.launchInContainer(
                OrganizerCreateNotificationFragment.class,
                argsForNotification("WAITING")
        );

        onView(withId(R.id.inputNotificationTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.inputNotifyMessage)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.07.02 – notify invited entrants
    // ---------------------------------------------------------
    @Test
    public void test_US_02_07_02() {
        FragmentScenario.launchInContainer(
                OrganizerCreateNotificationFragment.class,
                argsForNotification("INVITED")
        );

        onView(withId(R.id.inputNotificationTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.inputNotifyMessage)).check(matches(isDisplayed()));
    }

    // ---------------------------------------------------------
    // US 02.07.03 – notify cancelled entrants
    // ---------------------------------------------------------
    @Test
    public void test_US_02_07_03() {
        FragmentScenario.launchInContainer(
                OrganizerCreateNotificationFragment.class,
                argsForNotification("CANCELLED")
        );

        onView(withId(R.id.inputNotificationTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.inputNotifyMessage)).check(matches(isDisplayed()));
    }
}
