package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.contrib.RecyclerViewActions;

import org.junit.Test;

public class WaitlistTests {
    @Test
    // - US 01.01.01 – Join waiting list
    public void testJoinWaitingList() { // - US 01.01.01 – Join waiting list
        // Navigate to event details
        onView(withId(R.id.event_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Tap "Join Waiting List"
        onView(withId(R.id.waitlistButton)).perform(click());

        // Verify confirmation
        onView(withText("You have joined the waiting list"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLeaveWaitingList() { // US 01.01.02 – Leave waiting list
        onView(withId(R.id.event_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Tap the same waitlistButton (now showing "Leave Waiting List")
        onView(withId(R.id.waitlistButton)).perform(click());

        onView(withText("You have left the waiting list"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testViewWaitlistCount() { // US 01.05.04 – View waitlist count
        onView(withId(R.id.waitlistCount))
                .check(matches(withText("Total entrants: 10")));
    }
}
