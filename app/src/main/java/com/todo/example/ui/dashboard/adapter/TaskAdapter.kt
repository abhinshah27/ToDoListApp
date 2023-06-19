package com.todo.example.ui.dashboard.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.todo.example.R
import com.todo.example.data.datasource.local.entity.TaskEntity
import com.todo.example.databinding.ItemTaskBinding
import com.todo.example.ui.dashboard.adapter.TaskAdapter.ItemViewHolder
import com.todo.example.utils.TaskStatus
import com.todo.example.utils.hide
import com.todo.example.utils.isDateInPast
import com.todo.example.utils.show

/**
 * This class is responsible for converting each data entry [TaskEntity]
 * into [ItemViewHolder] that can then be added to the AdapterView for Task details data
 */
class TaskAdapter(
    private val context: Context,
    private val deleteEntity: (item: TaskEntity) -> Unit,
    private val checkTask: (id: Int, status: String) -> Unit
) :
    ListAdapter<TaskEntity, TaskAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemData: TaskEntity) = binding.apply {
            model = itemData
            executePendingBindings()
            imgRemoveTask.setOnClickListener {
                deleteEntity.invoke(itemData)
            }
            if (itemData.status == TaskStatus.COMPLETED.name) {
                txtTaskTittle.setTextColor(ContextCompat.getColor(context, R.color.black_90))
                txtTaskTittle.paintFlags = txtTaskTittle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                txtTaskStatus.hide()
            } else if (itemData.status == TaskStatus.UNCOMPLETED.name) {
                txtTaskTittle.paintFlags = 0
                if (isDateInPast(itemData.time)) {
                    txtTaskTittle.setTextColor(ContextCompat.getColor(context, R.color.red))
                    txtTaskStatus.text = TaskStatus.PENDING.name
                    txtTaskStatus.show()
                } else {
                    txtTaskTittle.setTextColor(ContextCompat.getColor(context, R.color.black_90))
                    txtTaskStatus.hide()
                }
            }
            cbTaskStatus.isChecked =
                !(itemData.status == TaskStatus.UNCOMPLETED.name || itemData.status == TaskStatus.PENDING.name)
            cbTaskStatus.setOnCheckedChangeListener { checkBox, isChecked ->
                if (checkBox.isPressed) {
                    if (isChecked) {
                        txtTaskTittle.paintFlags =
                            txtTaskTittle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        txtTaskTittle.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black_90,
                            ),
                        )
                        txtTaskStatus.hide()
                        itemData.id?.let { checkTask.invoke(it, TaskStatus.COMPLETED.name) }
                    } else {
                        txtTaskTittle.paintFlags = 0
                        itemData.id?.let { checkTask.invoke(it, TaskStatus.UNCOMPLETED.name) }
                    }
                }
            }
        }
    }

    override fun getItemCount() = currentList.size
    class DiffCallback : DiffUtil.ItemCallback<TaskEntity>() {
        override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean =
            oldItem == newItem
    }
}