package com.geekbrains.tests.automotor

import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OpenOtheAppsTest {

    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())

    @Test
    fun test_OpenSettings() {
        uiDevice.pressHome()
        uiDevice.swipe(500, 1500, 500, 0, 5)
        val appViews = UiScrollable(UiSelector().scrollable(false))
        val settingsApp = appViews
            .getChildByText(UiSelector().className(TextView::class.java.name), "Settings")
        settingsApp.clickAndWaitForNewWindow()

        val settingsValidation = uiDevice.findObject(
            UiSelector().packageName("com.android.settings"))
        Assert.assertTrue(settingsValidation.exists())
    }

}