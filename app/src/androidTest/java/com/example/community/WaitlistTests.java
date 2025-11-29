package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WaitlistTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testJoinWaitingList() {
        // Step 1–2: SplashPage → RoleSelect → click "User"
        onView(withId(R.id.buttonUser)).perform(click());

        // Step 3: EntrantHome → click first event in event_list
        onView(withId(R.id.event_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Step 4: EventDescription → tap waitlistButton
        onView(withId(R.id.waitlistButton)).perform(click());

        // Verify confirmation
        onView(withText("You have joined the waiting list"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLeaveWaitingList() {
        onView(withId(R.id.buttonUser)).perform(click());
        onView(withId(R.id.event_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.waitlistButton)).perform(click());
        onView(withText("You have left the waiting list"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testViewWaitlistCount() {
        onView(withId(R.id.buttonUser)).perform(click());
        onView(withId(R.id.event_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.waitlistCount)).check(matches(isDisplayed()));
    }
}