package com.geekbrains.tests

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.geekbrains.tests.shared_test.BaseActivityTest
import com.geekbrains.tests.shared_test.QUERY_ALGOL
import com.geekbrains.tests.shared_test.waitFor
import com.geekbrains.tests.view.search.MainActivity
import com.geekbrains.tests.view.search.SearchResultAdapter
import org.hamcrest.CoreMatchers
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
            .perform(ViewActions.replaceText(query), ViewActions.closeSoftKeyboard())
        onView(searchEditText).perform(ViewActions.pressImeActionButton())
    }

    @Test
    fun activitySearch_testItemDataDisplayedCorrectly() {
        performSearch(QUERY_ALGOL)

        scenario.onActivity { activity ->
            var isDataLoaded = false
            var attempts = 0
            while (!isDataLoaded && attempts < 5) {
                waitFor(10000)

                val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
                val adapter = recyclerView.adapter as SearchResultAdapter

                if (adapter.itemCount > 0) {
                    val actualData = adapter.getData()[1]

                    val testingRecyclerView = withId(R.id.recyclerView)

                    onView(testingRecyclerView).perform(
                        RecyclerViewActions.scrollToPosition<SearchResultAdapter.SearchResultViewHolder>(1)
                    )

                    onView(withRecyclerViewItemId(R.id.recyclerView, 1, R.id.tv_id))
                        .check(matches(withText(actualData.id.toString())))

                    onView(withRecyclerViewItemId(R.id.recyclerView, 1, R.id.tv_name))
                        .check(matches(withText(actualData.name)))

                    onView(withRecyclerViewItemId(R.id.recyclerView, 1, R.id.tv_full_name))
                        .check(matches(withText(actualData.fullName)))

                    isDataLoaded = true
                }

                attempts++

            }
        }
    }

    private fun withRecyclerViewItemId(
        recyclerViewId: Int,
        position: Int,
        viewId: Int
    ) : Matcher<View> {
        return CoreMatchers.allOf(
            withId(viewId),
            isDescendantOfA(nthChildOf(withId(recyclerViewId), position))
        )
    }

    private fun nthChildOf(
        parentMatcher: Matcher<View>, childPosition: Int
    ) : Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.appendText("на позиции $childPosition")
                parentMatcher.describeTo(description)
            }

            override fun matchesSafely(item: View?): Boolean {
               if (item?.parent is ViewGroup) return false
                val parent = item?.parent as ViewGroup

                return parentMatcher.matches(parent) && parent.childCount > childPosition
                        && parent.getChildAt(childPosition) == item
            }
        }
    }
}