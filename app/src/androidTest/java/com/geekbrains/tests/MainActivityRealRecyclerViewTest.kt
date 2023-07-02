package com.geekbrains.tests

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.geekbrains.tests.shared_test.BaseActivityTest
import com.geekbrains.tests.shared_test.QUERY_ALGOL
import com.geekbrains.tests.shared_test.waitFor
import com.geekbrains.tests.view.search.MainActivity
import com.geekbrains.tests.view.search.SearchResultAdapter
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Test

class MainActivityRealRecyclerViewTest : BaseActivityTest<MainActivity>() {

    override fun getActivityClass() = MainActivity::class.java

    private fun performSearch(query: String) {
        val searchEditText = withId(R.id.searchEditText)
        onView(searchEditText).perform(ViewActions.click())
        onView(searchEditText)
            .perform(ViewActions.replaceText(query), closeSoftKeyboard())
        onView(searchEditText).perform(pressImeActionButton())
    }

    @Test
    fun activitySearch_testItemDataDisplayedCorrectly() {

        performSearch(QUERY_ALGOL)

        onView(isRoot()).perform(waitFor(10000))

        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.scrollToPosition<SearchResultAdapter.SearchResultViewHolder>(1)
        )

        onView(withRecyclerViewItemId(R.id.recyclerView, 1, R.id.tv_id))
            .check(matches(withText("12899649")))

        onView(withRecyclerViewItemId(R.id.recyclerView, 1, R.id.tv_name))
            .check(matches(withText("algoliasearch-rails")))

        onView(withRecyclerViewItemId(R.id.recyclerView, 1, R.id.tv_full_name))
            .check(matches(withText("algolia/algoliasearch-rails")))

    }

    private fun withRecyclerViewItemId(
        recyclerViewId: Int,
        position: Int,
        viewId: Int
    ): Matcher<View> {
        return allOf(
            withId(viewId),
            isDescendantOfA(nthChildOf(withId(recyclerViewId), position))
        )
    }

    private fun nthChildOf(
        parentMatcher: Matcher<View>, childPosition: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.appendText("на позиции $childPosition")
                parentMatcher.describeTo(description)
            }

            override fun matchesSafely(item: View?): Boolean {
                val parent = item?.parent
                if (parent !is ViewGroup) return false

                return parentMatcher.matches(parent) && parent.childCount > childPosition
                        && parent.getChildAt(childPosition) == item
            }
        }
    }

    private fun checkRecyclerViewItemText(
        position: Int,
        textViewId: Int,
        expectedText: String
    ): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "Проверка текстов в RecyclerView"

            override fun getConstraints() = allOf(
                isAssignableFrom(RecyclerView::class.java), isDisplayed()
            )

            override fun perform(uiController: UiController, view: View) {
                val recyclerView = view as RecyclerView
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                val actualText =
                    viewHolder?.itemView?.findViewById<TextView>(textViewId)?.text?.toString()

                if (actualText != expectedText) {
                    throw AssertionError(
                        "В TextView ожидался текст: \"$expectedText\", " +
                                "но обнаружен следующий текст: \"$actualText\" "
                    )
                }
            }
        }
    }

    @Test
    fun activitySearch_testTextDisplayedCorrectlyWithViewAction() {

        performSearch(QUERY_ALGOL)

        onView(isRoot()).perform(waitFor(10000))

        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.scrollToPosition<SearchResultAdapter
            .SearchResultViewHolder>(2)
        )

        onView(withId(R.id.recyclerView)).perform(
            checkRecyclerViewItemText(
                2, R.id.tv_id, "31758353"
            )
        )

        onView(withId(R.id.recyclerView)).perform(
            checkRecyclerViewItemText(
                2, R.id.tv_name, "algoliasearch-wordpress"
            )
        )

        onView(withId(R.id.recyclerView)).perform(
            checkRecyclerViewItemText(
                2, R.id.tv_full_name, "algolia/algoliasearch-wordpress"
            )
        )
    }
}
