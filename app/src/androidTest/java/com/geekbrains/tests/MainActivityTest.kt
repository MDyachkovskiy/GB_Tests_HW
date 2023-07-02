package com.geekbrains.tests


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.geekbrains.tests.shared_test.TEST_NUMBER_OF_RESULTS_PLUS_1
import com.geekbrains.tests.view.search.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

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
    fun mainActivityTest() {
        val materialButton = onView(withId(R.id.toDetailsActivityButton))
        materialButton.perform(click())

        val materialButton2 = onView(withId(R.id.incrementButton))
        materialButton2.perform(click())

        val textView = onView(withId(R.id.totalCountTextView))
        textView.check(matches(withText(TEST_NUMBER_OF_RESULTS_PLUS_1)))
        textView.check(matches(isDisplayed()))

        val button = onView(withId(R.id.incrementButton))
        button.check(matches(isDisplayed()))
    }
}
