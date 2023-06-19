package com.todo.example.ui.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.todo.example.R
import com.todo.example.data.common.onError
import com.todo.example.data.common.onLoading
import com.todo.example.data.common.onSuccess
import com.todo.example.data.datasource.local.entity.TaskEntity
import com.todo.example.databinding.FragmentTaskBinding
import com.todo.example.ui.dashboard.adapter.TaskAdapter
import com.todo.example.ui.dashboard.viewmodel.MainViewModel
import com.todo.example.utils.hide
import com.todo.example.utils.show
import com.todo.example.utils.showSnackBar
import kotlinx.coroutines.launch

/**
 * Task Listing screen
 */
class TaskFragment : Fragment() {

    private var adapter: TaskAdapter? = null
    private lateinit var binding: FragmentTaskBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var mPopupMenu: PopupMenu
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCollector()
        initClick()
        initData()
    }

    private fun initCollector() {
        lifecycleScope.launch {
            mainViewModel.resultTask.collect { handleResult ->
                handleResult.onLoading {
                }.onSuccess { data ->
                    binding.apply {
                        if (data.toMutableList().isNotEmpty()) {
                            txtEmptyView.hide()
                            rvTaskList.show()
                        } else {
                            rvTaskList.hide()
                            txtEmptyView.show()
                        }
                    }
                    adapter?.submitList(data.toMutableList())
                }.onError {
                }
            }
        }
    }

    private fun initData() {
        mainViewModel.getTaskList()
        binding.apply {
            adapter = TaskAdapter(requireContext(), deleteEntity = {
                deleteTask(it)
            }, checkTask = { id, status ->
                mainViewModel.updateTask(id, status)
            })
            rvTaskList.layoutManager = LinearLayoutManager(requireContext())
            rvTaskList.adapter = adapter

            mPopupMenu = PopupMenu(requireContext(), imgMore)
            mPopupMenu.menuInflater.inflate(R.menu.menu, mPopupMenu.menu)
            mPopupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item1 -> {
                        mainViewModel.sortTask(false)
                        true
                    }

                    R.id.item2 -> {
                        mainViewModel.sortTask(true)
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun initClick() {
        binding.apply {
            imgBack.setOnClickListener {
                requireActivity().showSnackBar(getString(R.string.work_in_progress), imgBack)
            }
            fabAddTask.setOnClickListener {
                findNavController().navigate(R.id.addTaskFragment)
            }
            imgMore.setOnClickListener {
                mPopupMenu.show()
            }
        }
    }

    private fun deleteTask(taskEntity: TaskEntity) {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle(getString(R.string.dialog_tittle))
            .setMessage("Do you want to delete \"${taskEntity.title}\", this action can’t be undone.")
            .setPositiveButton(getString(R.string.dialog_positive_btn_txt)) { dialog, _ ->
                dialog.dismiss()
                mainViewModel.deleteTask(taskEntity)
            }
            .setNegativeButton(getString(R.string.dialog_negative_btn_txt)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
