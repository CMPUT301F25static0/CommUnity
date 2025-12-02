package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OrganizerNotificationTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    // --- Runs before each test ---
    @Before
    public void navigateToNotifyScreen() throws InterruptedException {
        // Wait for splash screen to finish
        Thread.sleep(3000);

        // Login
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(3000);

        // Choose Host role
        onView(withId(R.id.buttonHost)).perform(click());
        Thread.sleep(3000);

        // (Optional) If organizer profile is required, create it here
        onView(withId(R.id.buttonMyProfile)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.name_box)).perform(typeText("TestFirstName"), closeSoftKeyboard());
        onView(withId(R.id.email_box)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.phone_box)).perform(typeText("7801234567"), closeSoftKeyboard());
        Thread.sleep(3000);
        onView(withId(R.id.save_button)).perform(click());
        Thread.sleep(2000);
    }

    @Test
    public void testNotifyEventFlow() throws InterruptedException {

        // --- Step 1: Click the Notify button ---
        onView(withId(R.id.buttonNotify))
                .perform(click());

        Thread.sleep(3000);

        // --- Step 2: Select the event "Winter Cup Tournament" ---
        onView(withId(R.id.notifyEventRecyclerView))
                .check(matches(hasDescendant(withText("Winter Cup Tournament"))))
                .perform(actionOnItem(
                        hasDescendant(withText("Winter Cup Tournament")),
                        click()));
        onView(withText("SEND TO WAITING LIST ENTRANTS")) // US 02.07.01 As an organizer I want to send notifications to all entrants on the waiting list
                .perform(click());

        Thread.sleep(3000);

        // --- Step 3: Fill in notification title and message ---
        onView(withId(R.id.inputNotificationTitle))
                .perform(typeText("Test Notification Title"), closeSoftKeyboard());

        onView(withId(R.id.inputNotifyMessage))
                .perform(typeText("This is a test notification message."), closeSoftKeyboard());

        Thread.sleep(1000);

        // --- Step 4: Send the notification ---
        onView(withId(R.id.buttonSend))
                .perform(click());

        Thread.sleep(3000);

        onView(withId(R.id.notifyEventRecyclerView))
                .check(matches(hasDescendant(withText("Winter Cup Tournament"))))
                .perform(actionOnItem(
                        hasDescendant(withText("Winter Cup Tournament")),
                        click()));
        onView(withText("SEND TO INVITED ENTRANTS")) //US 02.07.02 As an organizer I want to send notifications to all selected entrants
                .perform(click());

        // --- Step 3: Fill in notification title and message ---
        onView(withId(R.id.inputNotificationTitle))
                .perform(typeText("Test Notification Title"), closeSoftKeyboard());

        onView(withId(R.id.inputNotifyMessage))
                .perform(typeText("This is a test notification message."), closeSoftKeyboard());

        Thread.sleep(1000);

        // --- Step 4: Send the notification ---
        onView(withId(R.id.buttonSend))
                .perform(click());

        Thread.sleep(3000);
        onView(withId(R.id.notifyEventRecyclerView))
                .check(matches(hasDescendant(withText("Winter Cup Tournament"))))
                .perform(actionOnItem(
                        hasDescendant(withText("Winter Cup Tournament")),
                        click()));
        onView(withText("SEND TO CANCELLED ENTRANTS")) //US 02.07.03 As an organizer I want to send a notification to all cancelled entrants
                .perform(click());

        // --- Step 3: Fill in notification title and message ---
        onView(withId(R.id.inputNotificationTitle))
                .perform(typeText("Test Notification Title"), closeSoftKeyboard());

        onView(withId(R.id.inputNotifyMessage))
                .perform(typeText("This is a test notification message."), closeSoftKeyboard());

        Thread.sleep(1000);

        // --- Step 4: Send the notification ---
        onView(withId(R.id.buttonSend))
                .perform(click());


    }
}
