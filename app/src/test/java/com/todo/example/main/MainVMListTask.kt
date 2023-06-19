package com.todo.example.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.todo.example.BaseTest
import com.todo.example.data.common.HandleResult
import com.todo.example.data.datasource.local.LocalDataSource
import com.todo.example.data.repository.TaskRepositoryImpl
import com.todo.example.domain.usecase.GetTaskUseCase
import com.todo.example.utils.TEST_FAILED_DUE_TO_STATE_NOT_LOADING
import com.todo.example.utils.TEST_FAILED_DUE_TO_STATE_NOT_SUCCESS
import com.todo.example.ui.dashboard.callback.DatabaseOperationInterface
import com.todo.example.ui.dashboard.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainVMListTask : BaseTest() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataSource: LocalDataSource

    private lateinit var repositoryImpl: TaskRepositoryImpl

    private lateinit var getTaskUseCase: GetTaskUseCase

    private lateinit var mainViewModel: MainViewModel

    override fun setUp() {
        super.setUp()

        repositoryImpl = TaskRepositoryImpl(dataSource)
        getTaskUseCase = GetTaskUseCase(repositoryImpl)
        mainViewModel = MainViewModel(getTaskUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun listTaskSuccess() = runTest {
        Mockito.`when`(dataSource.getAllTask(any())).thenAnswer {
            val callback: DatabaseOperationInterface = it.arguments[0] as DatabaseOperationInterface
            callback.getList(arrayListOf())
        }

        mainViewModel.resultTask.test {
            mainViewModel.getTaskList()
            var result = awaitItem()
            Assert.assertTrue(TEST_FAILED_DUE_TO_STATE_NOT_LOADING, result is HandleResult.Loading)
            result = awaitItem()
            Assert.assertTrue(TEST_FAILED_DUE_TO_STATE_NOT_SUCCESS, result is HandleResult.Success)
        }
    }
}
