package com.geekbrains.tests.automotor

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.geekbrains.tests.R
import com.geekbrains.tests.shared_test.BUTTON_TO_DETAILS_ACTIVITY
import com.geekbrains.tests.shared_test.EDIT_TEXT_SEARCH
import com.geekbrains.tests.shared_test.RESULT_42
import com.geekbrains.tests.shared_test.TEST_NUMBER_OF_RESULTS_ZERO
import com.geekbrains.tests.shared_test.TV_TOTAL_COUNT
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BehaviorTest {

    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val packageName = context.packageName

    @Before
    fun setup() {
        uiDevice.pressHome()

        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    companion object {
        private const val TIMEOUT = 10000L
    }

    @Test
    fun test_MainActivityIsStarted() {
        val editText = uiDevice.findObject(By.res(packageName, EDIT_TEXT_SEARCH))
        Assert.assertNotNull(editText)
    }

    @Test
    fun test_SearchIsPositive() {
        val editText = uiDevice.findObject(By.res(packageName, EDIT_TEXT_SEARCH))
        editText.text = "UiAutomator"

        Espresso.onView(ViewMatchers.withId(R.id.searchEditText))
            .perform(ViewActions.pressImeActionButton())

        val changedText = uiDevice.wait(
            Until.findObject(By.res(packageName, TV_TOTAL_COUNT)), TIMEOUT)

        Assert.assertEquals(changedText.text.toString(), RESULT_42)
    }

    @Test
    fun test_OpenDetailsScreen() {
        val toDetails = uiDevice.findObject(By.res(packageName, BUTTON_TO_DETAILS_ACTIVITY))
        toDetails.click()

        val changedText = uiDevice.wait(
            Until.findObject(By.res(packageName, TV_TOTAL_COUNT)), TIMEOUT)
        Assert.assertEquals(changedText.text, TEST_NUMBER_OF_RESULTS_ZERO)
    }

}