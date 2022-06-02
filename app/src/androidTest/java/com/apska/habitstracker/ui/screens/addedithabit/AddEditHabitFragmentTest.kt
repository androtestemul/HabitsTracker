package com.apska.habitstracker.ui.screens.addedithabit

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
//import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Assert.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import com.apska.habitstracker.R
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
internal class AddEditHabitFragmentTest {
    /*@get:Rule
    val */

    private lateinit var scenario: FragmentScenario<AddEditHabitFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(fragmentArgs = bundleOf("habitId" to -1), themeResId = R.style.Theme_HabitsTracker)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun test1() {
        val header = "New Habit Header";
        val description = "New Habit Description";

        onView(withId(R.id.headerEditText)).perform(typeText(header))
        onView(withId(R.id.descriptionEditText)).perform(typeText(description))

        Espresso.closeSoftKeyboard()

        //onView(withId(R.id.addEditHabitMainScroll)).perform(scrollTo(), click())
        onView(withId(R.id.saveButton)).perform(scrollTo())

        onView(withId(R.id.saveButton)).perform(click())

        //assertThat(onView(withId(R.id.periodTextInputLayout)).check(matches(withText("Bla bla bla"))))
        onView(withId(R.id.periodTextInputLayout)).check(matches(withText("Bla bla bla")))

    }

}