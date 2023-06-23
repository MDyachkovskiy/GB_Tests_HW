package com.geekbrains.tests

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.search.MainActivity
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"),
            closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        if(BuildConfig.TYPE == MainActivity.FAKE) {
            onView(withId(R.id.totalCountTextView))
                .check(matches(withText("Number of results: 42")))
        } else {
            onView(isRoot()).perform(delay())
            onView(withId(R.id.totalCountTextView))
                .check(matches(withText("Number of results: 2283")))
        }

    }

    private fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for 2 seconds"
            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(2000)
            }
        }
    }

    @Test
    fun testVisibilityOfTotalCountTextViewAfterDisplaySearchResults() {
        onView(withId(R.id.totalCountTextView))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
    }

    @Test
    fun checkIfEditTextIsVisible() {
        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfButtonIsVisible() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfProgressBarIsNotVisibleOnLaunch() {
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun checkToastMessageWhenSearchQueryIsEmpty() {
        scenario.onActivity {
            onView(withId(R.id.searchEditText))
                .perform(typeText(""), pressImeActionButton())
            onView(withText(R.string.enter_search_word))
                .inRoot(ToastMatcher())
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun checkIfTextIsEnteredInEditText() {
        onView(withId(R.id.searchEditText))
            .perform(typeText("query"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText))
            .check(matches(withText("query")))
    }
}