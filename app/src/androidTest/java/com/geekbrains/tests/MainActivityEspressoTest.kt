package com.geekbrains.tests

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.shared_test.BaseActivityTest
import com.geekbrains.tests.shared_test.EMPTY_QUERY
import com.geekbrains.tests.shared_test.QUERY_ALGOL
import com.geekbrains.tests.shared_test.RESULT_ALGOL
import com.geekbrains.tests.shared_test.waitFor
import com.geekbrains.tests.view.details.DetailsActivity
import com.geekbrains.tests.view.search.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest : BaseActivityTest<MainActivity>() {

    @Before
    override fun setup() {
        super.setup()
        Intents.init()
    }

    @After
    override fun close() {
        super.close()
        Intents.release()
    }

    override fun getActivityClass() = MainActivity::class.java

    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText(QUERY_ALGOL), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        onView(isRoot()).perform(waitFor(8000))
        onView(withId(R.id.totalCountTextView)).check(matches(withText(RESULT_ALGOL)))
    }

    @Test
    fun testByIntent_toDetailsActivityButton_launchesDetailsActivity_withCorrectRepositoryCount() {
        onView(withId(R.id.searchEditText)).perform(click(),
        replaceText(QUERY_ALGOL), closeSoftKeyboard())
        onView(withId(R.id.searchButton)).perform(click())

        onView(isRoot()).perform(waitFor(5000))

        onView(withId(R.id.totalCountTextView)).check(matches(withText(RESULT_ALGOL)))

        onView(withId(R.id.toDetailsActivityButton)).perform(click())

        intended(
            allOf(
                hasComponent(DetailsActivity::class.java.name),
                hasExtra(equalTo(DetailsActivity.TOTAL_COUNT_EXTRA), equalTo(3804))
            )
        )
    }

    @Test
    fun testByTextView_toDetailsActivityButton_launchesDetailsActivity_withCorrectRepositoryCount() {
        onView(withId(R.id.searchEditText)).perform(click(),
            replaceText(QUERY_ALGOL), closeSoftKeyboard())
        onView(withId(R.id.searchButton)).perform(click())

        onView(isRoot()).perform(waitFor(5000))

        onView(withId(R.id.totalCountTextView)).check(matches(withText(RESULT_ALGOL)))

        onView(withId(R.id.toDetailsActivityButton)).perform(click())

        onView(isRoot()).perform(waitFor(5000))

        val countText = String.format(
            Locale.getDefault(), "Number of results: %d",3804)
        onView(withId(R.id.totalCountTextView)).check(matches(withText(countText)))

    }

    @Test
    fun testSearchButtonOnClickEmptyQueryShowEmptyTextToast() {
        onView(withId(R.id.searchEditText)).perform(clearText())
        onView(withId(R.id.searchButton)).perform(click())
        onView(withText(R.string.enter_search_word)).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun testSearchButtonOnClickWithQueryNotShowEmptyTextToast() {
        onView(withId(R.id.searchEditText))
            .perform(replaceText(QUERY_ALGOL), closeSoftKeyboard())
        onView(withId(R.id.searchButton)).perform(click())
        onView(withText(R.string.enter_search_word)).inRoot(ToastMatcher())
            .check(doesNotExist())
    }

    @Test
    fun testSearchButtonOnClickWithQueryDisplayResults() {
        onView(withId(R.id.searchEditText))
            .perform(replaceText(QUERY_ALGOL), closeSoftKeyboard())
        onView(withId(R.id.searchButton)).perform(click())
        waitFor(8000)
        onView(withId(R.id.totalCountTextView)).check(matches(isDisplayed()))
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
            onView(withId(R.id.searchEditText))
                .perform(typeText(EMPTY_QUERY), pressImeActionButton())
            onView(withText(R.string.enter_search_word))
                .inRoot(ToastMatcher())
                .check(matches(isDisplayed()))
    }

    @Test
    fun checkIfTextIsEnteredInEditText() {
        onView(withId(R.id.searchEditText))
            .perform(typeText(QUERY_ALGOL), closeSoftKeyboard())
        onView(withId(R.id.searchEditText))
            .check(matches(withText(QUERY_ALGOL)))
    }
}