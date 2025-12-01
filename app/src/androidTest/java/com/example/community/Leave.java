package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import android.content.Context;
import android.provider.Settings;
import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Leave {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void disableAnimations() {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Settings.Global.putFloat(targetContext.getContentResolver(),
                Settings.Global.WINDOW_ANIMATION_SCALE, 0f);
        Settings.Global.putFloat(targetContext.getContentResolver(),
                Settings.Global.TRANSITION_ANIMATION_SCALE, 0f);
        Settings.Global.putFloat(targetContext.getContentResolver(),
                Settings.Global.ANIMATOR_DURATION_SCALE, 0f);
    }

    @Test
    public void leaveWaitlist() throws InterruptedException {
        // Click login button
        onView(isRoot()).perform(waitForView(R.id.loginButton, 5000));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed())).perform(click());

        // Click User button
        onView(isRoot()).perform(waitForView(R.id.buttonUser, 5000));
        onView(withId(R.id.buttonUser)).check(matches(isDisplayed())).perform(click());

        // Click first event in RecyclerView
        onView(isRoot()).perform(waitForView(R.id.event_list, 5000));
        onView(withId(R.id.event_list)).perform(actionOnItemAtPosition(0, click()));

        // Press Join Waitlist button
        onView(isRoot()).perform(waitForView(R.id.waitlistButton, 5000));
        onView(withId(R.id.waitlistButton)).check(matches(isDisplayed())).perform(scrollTo(), click());

        // Wait 3 seconds before leaving
        Thread.sleep(3000);

        // Press Leave Waitlist button (same button, now text changed)
        onView(isRoot()).perform(waitForView(R.id.waitlistButton, 5000));
        onView(withId(R.id.waitlistButton)).check(matches(isDisplayed()))
                .check(matches(withText("Leave waitlist")))
                .perform(scrollTo(), click());
    }

    // Custom ViewAction to wait for a view with a specific ID
    public static ViewAction waitForView(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "wait up to " + millis + " milliseconds for view with id " + viewId;
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (child.getId() == viewId && child.isShown()) {
                            return;
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50);
                } while (System.currentTimeMillis() < endTime);

                throw new NoMatchingViewException.Builder()
                        .withViewMatcher(withId(viewId))
                        .build();
            }
        };
    }
}
