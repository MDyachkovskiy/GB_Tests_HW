package com.geekbrains.tests.shared_test

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

abstract class BaseActivityTest<T : Activity> {
    protected lateinit var scenario: ActivityScenario<T>
    protected lateinit var context: Context

    @Before
    open fun setup() {
        scenario = ActivityScenario.launch(getActivityClass())
        context = ApplicationProvider.getApplicationContext()
    }

    @After
    open fun close() {
        scenario.close()
    }

    @Test
    fun activityAssertNotNull() {
        scenario.onActivity {
            assertNotNull(it)
        }
    }

    @Test
    fun activityIsResumed() {
        assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    abstract fun getActivityClass(): Class<T>
}