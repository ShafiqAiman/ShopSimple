package com.ruth.checkmeout;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.ruth.checkmeout.ui.LogInFragment;
import com.ruth.checkmeout.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkLoginFragmentOpened() {
        LogInFragment fragment=new LogInFragment();
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.flContent,fragment).commit();
    }

    @Test
    public void LogInInfoSentToMyAccount() {
        LogInFragment fragment=new LogInFragment();
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.flContent,fragment).commit();
        onView(withId(R.id.logInEmail)).perform(replaceText("rwmwangi96@gmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.logInPassword)).perform(typeText("Trinidad96")).perform(closeSoftKeyboard());
        try {                             // the sleep method requires to be checked and handled so we use try block
            Thread.sleep(250);
        } catch (InterruptedException e){
            System.out.println("got interrupted!");
        }
        onView(withId(R.id.logInButton)).perform(click());


    }
}
