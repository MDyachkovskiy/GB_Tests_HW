package com.geekbrains.tests

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.geekbrains.tests.presenter.search.PresenterSearchContract
import com.geekbrains.tests.view.search.MainActivity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLooper
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var context: Context
    private lateinit var presenter: PresenterSearchContract

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        context = ApplicationProvider.getApplicationContext()
        presenter = mock(PresenterSearchContract::class.java)
    }

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun onCreate_isCalled_onActivityCreation() {
        scenario.onActivity{
            assertNotNull(it)
        }
    }

    @Test
    fun activity_isResumed() {
        assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun whenQueryIsNotBlank_searchGitHubCalled() {
        scenario.onActivity {
            val searchEditText = it.findViewById<EditText>(R.id.searchEditText)
            searchEditText.setText("query", TextView.BufferType.EDITABLE)
            assertNotNull(searchEditText.text)
            assertEquals("query", searchEditText.text.toString())
        }
    }

    @Test
    fun whenQueryIsEmpty_showToastMessage() {
        scenario.onActivity {
            val searchEditText = it.findViewById<EditText>(R.id.searchEditText)
            searchEditText.setText("")
            searchEditText.onEditorAction(EditorInfo.IME_ACTION_SEARCH)

            val expectedToast = it.getString(R.string.enter_search_word)
            val actualToast = ShadowToast.getTextOfLatestToast()
            assertEquals(expectedToast, actualToast)
        }
    }

    @Test
    fun testDisplayError() {
        scenario.onActivity {
            it.displayError()
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
            assertEquals(it.getString(R.string.undefined_error),
            ShadowToast.getTextOfLatestToast())
        }
    }

    @Test
    fun testDisplayErrorWithSpecificError() {
        scenario.onActivity {
            val error = "Test error"
            it.displayError(error)
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
            assertEquals(error, ShadowToast.getTextOfLatestToast())
        }
    }

    @Test
    fun testVisibilityOfDisplayLoading() {
        scenario.onActivity {
            it.displayLoading(true)
            assertEquals(View.VISIBLE, it._binding.progressBar.visibility)

            it.displayLoading(false)
            assertEquals(View.GONE, it._binding.progressBar.visibility)
        }
    }
}