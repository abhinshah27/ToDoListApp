package com.todo.example.main

import android.view.View
import android.widget.TimePicker
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.todo.example.AM
import com.todo.example.DELAY_1000
import com.todo.example.DELAY_2000
import com.todo.example.OK
import com.todo.example.R
import com.todo.example.TEST_FAILED
import com.todo.example.TEST_WORD_1
import com.todo.example.di.AppModule
import com.todo.example.ui.dashboard.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import java.util.Calendar
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@UninstallModules(AppModule::class)
@HiltAndroidTest
class AddTaskTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val uiDevice = UiDevice.getInstance(getInstrumentation())

    @get:Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun addTaskWorkingTest() {
        Thread.sleep(DELAY_2000)
        val fabAddTask =
            onView(withId(R.id.fab_add_task)).check(matches(isDisplayed()))
        fabAddTask.perform(click())
        Thread.sleep(DELAY_2000)
        onView(withId(R.id.ed_title)).perform(replaceText(TEST_WORD_1))

        val edTime =
            onView(withId(R.id.ed_time)).check(matches(isDisplayed()))
        edTime.perform(click())
        Thread.sleep(DELAY_1000)
        val calendar = Calendar.getInstance()
        onView(isAssignableFrom(TimePicker::class.java)).perform(
            PickerActions.setTime(
                calendar.get(Calendar.HOUR_OF_DAY) + 1,
                calendar.get(Calendar.MINUTE) + 2,
            ),
        )
        Thread.sleep(DELAY_1000)

        val labelOk =
            uiDevice.findObject(By.text(OK))
        labelOk?.click()

        val spinnerAmPm =
            onView(withId(R.id.til_meridiem_am_pm)).check(matches(isDisplayed()))
        spinnerAmPm.perform(click())
        Thread.sleep(DELAY_1000)

        val labelAM =
            uiDevice.findObject(By.text(AM))
        labelAM?.click()
        Thread.sleep(DELAY_1000)

        val btnAdd =
            onView(withId(R.id.btn_add)).check(matches(isDisplayed()))
        btnAdd.perform(click())
        Thread.sleep(DELAY_1000)
        val rvTaskList =
            mActivityRule.activity.findViewById<RecyclerView>(R.id.rv_task_list)
        Assert.assertTrue(TEST_FAILED, rvTaskList.visibility == View.VISIBLE)
    }
}
