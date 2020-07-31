package com.zlodeuscomp.vdolgah;

import android.widget.TextView;

import static androidx.test.espresso.action.ViewActions.clearText;
import static org.junit.Assert.*;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.zlodeuscomp.vdolgah.screens.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public void addTestDebtor() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.input_name)).perform(typeText("for test only #"), closeSoftKeyboard());
        onView(withId(R.id.input_sum)).perform(typeText("1488"),closeSoftKeyboard());
        onView(withId(R.id.add)).perform(click());
    }

    public void deleteTestDebtor() {
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
    }

//    @Before
//    public void setUp() throws Exception {
//        onView(withId(R.id.fab)).perform(click());
//        onView(withId(R.id.input_name)).perform(typeText("for test only #"), closeSoftKeyboard());
//        onView(withId(R.id.input_sum)).perform(typeText("1488"),closeSoftKeyboard());
//        onView(withId(R.id.add)).perform(click());
//    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void delete_debtor_with_long_click() {
        RecyclerView recyclerView = mActivityRule.getActivity().findViewById(R.id.list);
        int startCount = recyclerView.getAdapter().getItemCount();

        addTestDebtor();
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        int endCount = recyclerView.getAdapter().getItemCount();
        assertEquals(endCount, startCount);
    }

    @Test
    public void update_debtor_name_with_click() {

        addTestDebtor();
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.input_name)).perform(clearText(), typeText("new name"), closeSoftKeyboard());
        onView(withId(R.id.add)).perform(click());

        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.input_name)).check(matches(withText("new name")));

        onView(withId(R.id.add)).perform(click());
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
    }

    @Test
    public void sum_updates_after_adding_new_debtor() {
        TextView textView = (TextView) mActivityRule.getActivity().findViewById(R.id.total_sum);
        int previousSum = Integer.parseInt((textView.getText().toString()));

        addTestDebtor();
        int newSum = Integer.parseInt((textView.getText().toString()));

        assertNotEquals(previousSum, newSum);
        deleteTestDebtor();
    }

}
