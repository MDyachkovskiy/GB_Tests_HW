package com.geekbrains.tests

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.geekbrains.tests.presenter.search.PresenterSearchContract
import com.geekbrains.tests.view.search.MainActivity
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest : KoinComponent {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private val presenter: PresenterSearchContract = mock(PresenterSearchContract::class.java)

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)

        val app = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as TestApp
        app.injector = module {
            factory<PresenterSearchContract> {presenter}
        }

        startKoin {
            modules(app.injector)
        }
    }

    @After
    fun close() {
        scenario.close()
        stopKoin()
    }

    @Test
    fun testVisibilityOfTotalCountTextViewAfterDisplaySearchResults() {
        onView(withId(R.id.totalCountTextView))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
    }

    @Test
    fun onActivityStart_presenterOnAttachCalled() {
        scenario.onActivity {
            verify(presenter).onAttach(any())
        }
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
                .perform(typeText(""), pressImeActionButton())
            onView(withText(R.string.enter_search_word))
                .inRoot(ToastMatcher())
                .check(matches(isDisplayed()))
    }

    @Test
    fun checkIfTextIsEnteredInEditText() {
        onView(withId(R.id.searchEditText))
            .perform(typeText("query"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText))
            .check(matches(withText("query")))
    }
}