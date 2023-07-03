package com.geekbrains.tests.shared_test

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

const val TEST_NUMBER = 42
const val TEST_NUMBER_OF_RESULTS_ZERO = "Number of results: 0"
const val TEST_NUMBER_OF_RESULTS_PLUS_1 = "Number of results: 1"
const val TEST_NUMBER_OF_RESULTS_MINUS_1 = "Number of results: -1"
const val QUERY_ALGOL = "algol"
const val RESULT_ALGOL = "Number of results: 3804"
const val RESULT_42 = "Number of results: 42"
const val EMPTY_QUERY = ""

const val BUTTON_TO_DETAILS_ACTIVITY = "toDetailsActivityButton"
const val EDIT_TEXT_SEARCH = "searchEditText"
const val TV_TOTAL_COUNT = "totalCountTextView"

fun waitFor(delay: Long): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String = "wait for $2 seconds"

        override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()

        override fun perform(uiController: UiController?, view: View?) {
            uiController?.loopMainThreadForAtLeast(delay)
        }
    }
}