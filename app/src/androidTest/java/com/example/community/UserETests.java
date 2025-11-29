package com.example.community;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.action.ViewActions.clearText;


import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import androidx.test.espresso.contrib.RecyclerViewActions;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserETests {

    @Rule// MIGHT BE WRONG
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);



    @Test
    public void testLeaveWaitingList() {
        onView(withId(R.id.event_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Tap the same waitlistButton (now showing "Leave Waiting List")
        onView(withId(R.id.waitlistButton)).perform(click());

        onView(withText("You have left the waiting list"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSeeEventList() {
        onView(withId(R.id.event_list))
                .check(matches(isDisplayed()));
    }

    //@Test
   // public void testFilterEvents() {
      //  onView(withId(R.id.filterButton)).perform(click());
      //  onView(withId(R.id.filter_interest)).perform(typeText("Music"), closeSoftKeyboard());
      //  onView(withId(R.id.apply_filter_button)).perform(click());

       // onView(withId(R.id.event_list))
               // .check(matches(hasDescendant(withText("Music Festival"))));
    }


   // @Test
   // public void testViewEventHistory() {
       // onView(withId(R.id.event_history)).perform(click());
        //onView(withId(R.id.event_history_list))
          //      .check(matches(isDisplayed()));
  //  }


    //@Test
   // public void testNotificationWin() {
        // Simulate notification
     //   onView(withText("Congratulations! You have been selected"))
       //         .check(matches(isDisplayed()));
    //}

    //@Test
    //public void testNotificationLose() {
      //  onView(withText("Sorry, you were not selected"))
        //        .check(matches(isDisplayed()));
    //}

    //@Test
    //public void testOptOutNotifications() {
      //  onView(withId(R.id.notification_settings_button)).perform(click());
       // onView(withId(R.id.opt_out_checkbox)).perform(click());

     //   onView(withText("Notifications disabled")).check(matches(isDisplayed()));
    //}

    //@Test
    //public void testAnotherChance() {
     //   onView(withText("You have another chance")).check(matches(isDisplayed()));
    //}

    //@Test
    //public void testAcceptInvitation() {
      //  onView(withId(R.id.accept_invite_button)).perform(click());
        //onView(withText("You are registered")).check(matches(isDisplayed()));
    //}

    //@Test
    //public void testDeclineInvitation() {
     //   onView(withId(R.id.decline_invite_button)).perform(click());
       // onView(withText("You declined the invitation")).check(matches(isDisplayed()));
    //}

    //@Test
    //public void testViewWaitlistCount() {
      //  onView(withId(R.id.waitlist_count))
        //        .check(matches(withText("Total entrants: 10")));
    //}

    //@Test
    //public void testLotteryGuidelinesDisplayed() {
      //  onView(withId(R.id.lottery_guidelines))
        //        .check(matches(isDisplayed()));
    //}

    //@Test
    //public void testScanQrCodeShowsEventDetails() {
      //  onView(withId(R.id.entrantQRScanner)).perform(click());
        //intended(hasComponent(EventDetailsActivity.class.getName()));
    //}

    //@Test
    //public void testSignUpFromEventDetails() {
     //   onView(withId(R.id.sign_up_button)).perform(click());
       // onView(withText("You are registered")).check(matches(isDisplayed()));
    //}

    //@Test
    //public void testDeviceIdentification() {
      //  onView(withText("Welcome back, Josiah"))
        //        .check(matches(isDisplayed()));
    //}
