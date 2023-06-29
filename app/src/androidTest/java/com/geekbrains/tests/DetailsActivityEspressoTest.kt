package com.geekbrains.tests

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.shared_test.BaseActivityTest
import com.geekbrains.tests.shared_test.TEST_NUMBER_OF_RESULTS_MINUS_1
import com.geekbrains.tests.shared_test.TEST_NUMBER_OF_RESULTS_PLUS_1
import com.geekbrains.tests.shared_test.TEST_NUMBER_OF_RESULTS_ZERO
import com.geekbrains.tests.view.details.DetailsActivity
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsActivityEspressoTest : BaseActivityTest<DetailsActivity>() {

    override fun getActivityClass() = DetailsActivity::class.java

    @Test
    fun activityTextView_NotNull() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            TestCase.assertNotNull(totalCountTextView)
        }
    }

    @Test
    fun activityTextView_HasText() {
        val assertion: ViewAssertion = matches(withText(TEST_NUMBER_OF_RESULTS_ZERO))
        onView(withId(R.id.totalCountTextView)).check(assertion)
    }

    @Test
    fun activityTextView_isDisplayed() {
        onView(withId(R.id.totalCountTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun activityTextView_isCompletelyDisplayed() {
        onView(withId(R.id.totalCountTextView)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun activityButtons_AreEffectiveVisible() {
        onView(withId(R.id.incrementButton))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(withId(R.id.decrementButton))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun activityButtonIncrement_IsWorking() {
        onView(withId(R.id.incrementButton)).perform(click())
        onView(withId(R.id.totalCountTextView))
            .check(matches(withText(TEST_NUMBER_OF_RESULTS_PLUS_1)))
    }

    @Test
    fun activityButtonDecrement_isWorking() {
        onView(withId(R.id.decrementButton)).perform(click())
        onView(withId(R.id.totalCountTextView))
            .check(matches(withText(TEST_NUMBER_OF_RESULTS_MINUS_1)))
    }

    @Test
    fun whenIncrementButtonClicked() {
        scenario.onActivity {
            it._binding.incrementButton.performClick()
            val expectedCount = TEST_NUMBER_OF_RESULTS_PLUS_1
            val actualCount = it._binding.totalCountTextView.text.toString()
            assertEquals(expectedCount, actualCount)
        }
    }

    @Test
    fun whenDecrementButtonClicked() {
        scenario.onActivity {
            it._binding.decrementButton.performClick()
            val expectedCount = TEST_NUMBER_OF_RESULTS_MINUS_1
            val actualCount = it._binding.totalCountTextView.text.toString()
            assertEquals(expectedCount,actualCount)
        }
    }
}