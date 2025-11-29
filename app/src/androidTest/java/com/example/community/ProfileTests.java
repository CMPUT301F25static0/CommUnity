package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Test;

public class ProfileTests {

    @Test
    public void testProvidePersonalInfo() { // US 01.02.01 – Provide personal information
        // No profile_button in your layout, so remove this line
        onView(withId(R.id.name_box)).perform(typeText("Josiah"), closeSoftKeyboard());
        onView(withId(R.id.email_box)).perform(typeText("josiah@example.com"), closeSoftKeyboard());
        onView(withId(R.id.phone_box)).perform(typeText("3061112222"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());

        onView(withText("Profile updated")).check(matches(isDisplayed()));
    }

    @Test
    public void testUpdateProfileInfo() { // US 01.02.02 – Update information
        onView(withId(R.id.email_box)).perform(clearText(), typeText("new@example.com"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());

        onView(withText("Profile updated")).check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteProfile() { // US 01.02.04 - Delete profile
        onView(withId(R.id.delete_unity)).perform(click());

        onView(withText("Profile deleted")).check(matches(isDisplayed()));
    }
}
