package com.example.community;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test. ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx. test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test. espresso.action.ViewActions.click;
import static androidx.test.espresso. assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EntrantEventDescriptionFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void navigatePastSplashScreen() throws InterruptedException {
        Thread.sleep(3500);

        onView(withId(R.id.loginButton))
                .check(matches(isDisplayed()));

        onView(withId(R.id.loginButton))
                .perform(click());

        Thread.sleep(3000);

        onView(withId(R.id.buttonUser))
                .check(matches(isDisplayed()));
    }

    /**
     * US 01.01.03: As an entrant, I want to be able to see a list of events that I can join the waiting list for.
     */
    @Test
    public void testNavigateToEntrantHome() throws InterruptedException {
        onView(withId(R.id.buttonUser))
                .check(matches(isDisplayed()));

        onView(withId(R.id.buttonUser))
                .perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.event_list))
                .check(matches(isDisplayed()));
    }

    /**
     * US 01.06.02: As an entrant, I want to be able to sign up for an event by from the event details.
     */
    @Test
    public void testNavigateToEventDescription() throws InterruptedException {
        navigateToEntrantHome();

        Thread.sleep(3000);

        onView(withId(R.id.event_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(2000);

        onView(withId(R.id. eventTitle))
                .check(matches(isDisplayed()));

        onView(withId(R.id. eventDescription))
                .check(matches(isDisplayed()));

        onView(withId(R.id.waitlistButton))
                .check(matches(isDisplayed()));
    }

    /**
     * US 01.01.01: As an entrant, I want to join the waiting list for a specific event
     */
    @Test
    public void testJoinWaitlist() throws InterruptedException {
        navigateToEventDescription();

        onView(withId(R.id.waitlistButton))
                . check(matches(isDisplayed()));

        onView(withId(R.id.waitlistButton))
                .perform(click());

        Thread.sleep(5000);

        onView(withId(R.id.waitlistButton))
                .check(matches(isDisplayed()));
    }

    /**
     * US 01.01.02: As an entrant, I want to leave the waiting list for a specific event
     */
    @Test
    public void testLeaveWaitlist() throws InterruptedException {
        navigateToEventDescription();

        onView(withId(R. id.waitlistButton))
                .perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.waitlistButton))
                .perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.waitlistButton))
                .check(matches(isDisplayed()));
    }

    /**
     * Navigation test: Verify back button returns to EntrantHomeFragment
     */
    @Test
    public void testBackButtonNavigation() throws InterruptedException {
        navigateToEventDescription();

        onView(withId(R.id.backButton))
                .perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.event_list))
                .check(matches(isDisplayed()));
    }

    /**
     * US 01.05.04: As an entrant, I want to know how many total entrants are on the waiting list for an event.
     * Verify all event details are displayed on the event description page
     */
    @Test
    public void testEventDetailsDisplay() throws InterruptedException {
        navigateToEventDescription();

        onView(withId(R. id.eventTitle))
                .check(matches(isDisplayed()));

        onView(withId(R.id.eventDescription))
                .check(matches(isDisplayed()));

        onView(withId(R.id.eventLocation))
                .check(matches(isDisplayed()));

        onView(withId(R.id. eventDates))
                .check(matches(isDisplayed()));

        onView(withId(R.id.registrationDates))
                .check(matches(isDisplayed()));

        onView(withId(R.id.capacity))
                .check(matches(isDisplayed()));

        onView(withId(R.id. eventOrganizerName))
                .check(matches(isDisplayed()));

        onView(withId(R. id.eventOrganizerEmail))
                .check(matches(isDisplayed()));

        onView(withId(R.id. waitlistCount))
                .check(matches(isDisplayed()));
    }

    // ============ Helper Methods =============

    private void navigateToEntrantHome() throws InterruptedException {
        onView(withId(R.id.buttonUser))
                .check(matches(isDisplayed()));

        onView(withId(R.id.buttonUser))
                .perform(click());

        Thread.sleep(4000);

        onView(withId(R.id.event_list))
                .check(matches(isDisplayed()));
    }

    private void navigateToEventDescription() throws InterruptedException {
        navigateToEntrantHome();

        Thread.sleep(2000);

        onView(withId(R.id.event_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(2000);

        onView(withId(R.id.eventTitle))
                .check(matches(isDisplayed()));
    }
}