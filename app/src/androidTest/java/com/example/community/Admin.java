package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.community.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Admin {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void admin() {

        // Click login button
        onView(withId(R.id.loginButton))
                .check(matches(isDisplayed()))
                .perform(click());

        // Wait for Admin button to appear and click
        onView(isRoot())
                .perform(waitForView(R.id.buttonAdmin, 5000));

        onView(withId(R.id.buttonAdmin))
                .check(matches(isDisplayed()))
                .perform(click());

        // Wait for Event button to appear and click
        onView(isRoot())
                .perform(waitForView(R.id.buttonEvent, 5000));

        onView(withId(R.id.buttonEvent))
                .check(matches(isDisplayed()))
                .perform(click());

        // Click first item in Admin Events RecyclerView
        onView(isRoot())
                .perform(waitForView(R.id.adminEventView, 5000));

        onView(withId(R.id.adminEventView))
                .perform(actionOnItemAtPosition(0, click()));
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
