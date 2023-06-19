package com.todo.example.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.google.android.material.imageview.ShapeableImageView
import com.todo.example.DELAY_2000
import com.todo.example.DELAY_5000
import com.todo.example.OK
import com.todo.example.R
import com.todo.example.TEST_FAILED
import com.todo.example.di.AppModule
import com.todo.example.ui.dashboard.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@UninstallModules(AppModule::class)
@HiltAndroidTest
class DeleteTaskTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @get:Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun addTaskWorkingTest() {
        Thread.sleep(DELAY_2000)
        val rvTaskList =
            mActivityRule.activity.findViewById<RecyclerView>(R.id.rv_task_list)
        if (rvTaskList.adapter?.itemCount != null) {
            mActivityRule.activity.runOnUiThread {
                val imgRemoveTask =
                    mActivityRule.activity.findViewById<ShapeableImageView>(R.id.img_remove_task)
                imgRemoveTask.performClick()
            }
            Thread.sleep(DELAY_5000)

            val labelOk =
                uiDevice.findObject(By.text(OK))
            labelOk?.click()
            Thread.sleep(DELAY_2000)

            rvTaskList.adapter?.itemCount?.let {
                Assert.assertTrue(TEST_FAILED, it == 0)
            }
        } else {
            Assert.fail(TEST_FAILED)
        }
    }
}
