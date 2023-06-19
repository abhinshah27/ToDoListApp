package com.todo.example.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.todo.example.DELAY_2000
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
class ListTaskTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun addTaskWorkingTest() {
        Thread.sleep(DELAY_2000)
        val rvTaskList =
            mActivityRule.activity.findViewById<RecyclerView>(R.id.rv_task_list)
        if (rvTaskList.adapter?.itemCount != null) {
            rvTaskList.adapter?.itemCount?.let {
                Assert.assertTrue(TEST_FAILED, it > 0)
            }
        } else {
            Assert.fail(TEST_FAILED)
        }
    }
}
