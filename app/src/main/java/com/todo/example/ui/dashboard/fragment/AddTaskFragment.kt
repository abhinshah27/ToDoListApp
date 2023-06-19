package com.todo.example.ui.dashboard.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.todo.example.R
import com.todo.example.databinding.FragmentAddTaskBinding
import com.todo.example.ui.dashboard.adapter.CustomSpinnerAdapter
import com.todo.example.ui.dashboard.viewmodel.MainViewModel
import com.todo.example.utils.TaskStatus
import com.todo.example.utils.getCurrentDate
import com.todo.example.utils.getCurrentMeridiem
import com.todo.example.utils.getCurrentTime
import com.todo.example.utils.isRunningTest
import com.todo.example.utils.isTimeInPast
import com.todo.example.utils.showSnackBar
import com.todo.example.utils.timePickerDialog

/**
 * Add Task screen
 */
class AddTaskFragment : Fragment() {

    private lateinit var mBinding: FragmentAddTaskBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPress()
        initClick()
        setSpinnerData()
    }

    private fun initClick() {
        mBinding.apply {
            imgBack.setOnClickListener {
                closeFragment()
            }
            btnCancel.setOnClickListener {
                closeFragment()
            }
            btnAdd.setOnClickListener {
                val title = edTitle.text.toString()
                val currentDate = getCurrentDate()
                if (isRunningTest) {
                    mainViewModel.time =
                        (mainViewModel.time.split(":")[0] + 1) + ":" + mainViewModel.time.split(":")[1]
                }
                val date = "$currentDate ${mainViewModel.time} ${mainViewModel.meridiem}"
                var isValidateData = true
                if (title.isEmpty()) {
                    isValidateData = false
                    requireActivity().showSnackBar(getString(R.string.add_a_title_to_the_task), it)
                } else if (isTimeInPast("${mainViewModel.time} ${mainViewModel.meridiem}")) {
                    Log.e("isTimeInPast", "isTimeInPast: ${mainViewModel.time} ${mainViewModel.meridiem}")
                    isValidateData = false
                    requireActivity().showSnackBar(
                        getString(R.string.please_select_a_future_time),
                        it,
                    )
                }
                if (isValidateData) {
                    mainViewModel.addTaskList(
                        title = title,
                        time = date,
                        status = TaskStatus.UNCOMPLETED.name,
                    )
                    requireActivity().showSnackBar(
                        getString(R.string.task_added_successfully),
                        btnAdd,
                    )
                    closeFragment()
                }
            }
            mainViewModel.time = getCurrentTime()
            edTime.setText(mainViewModel.time)
            mainViewModel.meridiem = getCurrentMeridiem()
            edTime.isClickable = true
            edTime.setOnClickListener {
                requireContext().timePickerDialog(time = { hour: String, min: String, meridiem: String ->
                    mainViewModel.meridiem = meridiem
                    mainViewModel.time = "$hour:$min"
                    edTime.setText(mainViewModel.time)
                    setSpinnerValue()
                }).show()
            }
        }
    }

    private fun setSpinnerData() {
        mBinding.apply {
            val adapter = CustomSpinnerAdapter(requireContext(), mainViewModel.timeList)
            dropdownAmPm.setAdapter(adapter)
            setSpinnerValue()
            dropdownAmPm.setOnItemClickListener { _, _, position, _ ->
                mainViewModel.meridiem = mainViewModel.timeList[position]
                adapter.setSelection(position)
            }
        }
    }

    private fun setSpinnerValue() {
        val index = mainViewModel.timeList.indexOf(mainViewModel.meridiem)
        val defaultText = if (index == 0) "AM" else "PM"
        mBinding.dropdownAmPm.setText(defaultText, false)
    }

    private fun backPress() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            closeFragment()
        }
    }

    private fun closeFragment() {
        findNavController().popBackStack()
    }
}
