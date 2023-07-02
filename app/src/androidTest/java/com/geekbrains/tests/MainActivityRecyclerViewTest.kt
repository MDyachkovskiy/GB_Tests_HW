package com.geekbrains.tests

import android.view.View
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.shared_test.BaseActivityTest
import com.geekbrains.tests.shared_test.QUERY_ALGOL
import com.geekbrains.tests.view.search.MainActivity
import com.geekbrains.tests.view.search.SearchResultAdapter
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityRecyclerViewTest : BaseActivityTest<MainActivity>() {

    override fun getActivityClass() = MainActivity::class.java

    @Test
    fun activitySearch_ScrollTo() {
        performSearch(QUERY_ALGOL)

        val recyclerView = withId(R.id.recyclerView)
        onView(recyclerView).perform(
            RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                hasDescendant(withText("FullName: 42"))
            )
        )
    }

    @Test
    fun activitySearch_PerformClickAtPosition() {
        performSearch(QUERY_ALGOL)

        val recyclerView = withId(R.id.recyclerView)
        onView(recyclerView).perform(
            RecyclerViewActions.actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                0, click()
            )
        )
    }

    @Test
    fun activitySearch_PerformClickOnItem() {
        performSearch(QUERY_ALGOL)

        val recyclerView = withId(R.id.recyclerView)
        onView(recyclerView).perform(
            RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                hasDescendant(withText("FullName: 50"))
            )
        )

        onView(recyclerView).perform(
            RecyclerViewActions.actionOnItem<SearchResultAdapter.SearchResultViewHolder>(
                hasDescendant(withText("FullName: 42")),
                click()
            )
        )
    }

    private fun performSearch(query: String) {
        val searchEditText = withId(R.id.searchEditText)
        onView(searchEditText).perform(click())
        onView(searchEditText).perform(replaceText(query), closeSoftKeyboard())
        onView(searchEditText).perform(pressImeActionButton())
    }

    private fun tapOnItemWithId(id: Int) = object : ViewAction {
        override fun getDescription() = "Нажимаем на view c указанным id"

        override fun getConstraints() = null

        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById(id) as View
            v.performClick()
        }
    }

    @Test
    fun activitySearch_PerformClick() {
        performSearch(QUERY_ALGOL)

        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                    0, tapOnItemWithId(R.id.checkbox)
                )
            )
    }

    private fun  typeTextInChildViewWithId(id: Int, textToBeTyped: String): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "Описание действия"

            override fun getConstraints() = null

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id) as EditText
                v.setText(textToBeTyped)
            }
        }
    }

    @Test
    fun activitySearch_performEditText() {
        performSearch(QUERY_ALGOL)

        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                    10,
                    typeTextInChildViewWithId(R.id.RVEditText,
                        "RecyclerView test")
                )
            )
    }
}