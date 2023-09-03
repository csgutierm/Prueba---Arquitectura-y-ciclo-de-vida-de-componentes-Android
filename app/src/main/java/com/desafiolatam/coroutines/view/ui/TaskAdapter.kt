package com.desafiolatam.coroutines.view.ui

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.desafiolatam.coroutines.data.TaskEntity
import com.desafiolatam.coroutines.databinding.ItemTaskBinding

class TaskAdapter :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private lateinit var binding: ItemTaskBinding
    lateinit var taskList: List<TaskEntity>

    var onLongClick: ((TaskEntity) -> Unit)? = null

    var onClick: ((TaskEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.onBind(taskList[position])
    }

    override fun getItemCount(): Int = taskList.size

    inner class TaskViewHolder(binding: ItemTaskBinding) : ViewHolder(binding.root) {
        private val checkBox = binding.checkbox
        private val taskTitle = binding.tvTaskTitle

        fun onBind(task: TaskEntity) {
            binding.run {
                tvTaskTitle.text = task.title
                tvTaskDescription.text = task.description



                clItem.setOnLongClickListener {
                    onLongClick?.invoke(task)
                    true
                }

                checkBox.isChecked = task.completada

                val spannableString = SpannableString(taskTitle.text)

                if (checkBox.isChecked) {
                    // Aplicar tachado
                    spannableString.setSpan(StrikethroughSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                } else {
                    // Quitar tachado
                    val strikethroughSpans = spannableString.getSpans(0, spannableString.length, StrikethroughSpan::class.java)
                    for (strikethroughSpan in strikethroughSpans) {
                        spannableString.removeSpan(strikethroughSpan)
                    }
                }

                taskTitle.text = spannableString

                clItem.setOnClickListener  {
                    onClick?.invoke(task)

                    checkBox.isChecked = !checkBox.isChecked

                    val spannableString = SpannableString(taskTitle.text)

                    if (checkBox.isChecked) {
                        // Aplicar tachado
                        spannableString.setSpan(StrikethroughSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    } else {
                        // Quitar tachado
                        val strikethroughSpans = spannableString.getSpans(0, spannableString.length, StrikethroughSpan::class.java)
                        for (strikethroughSpan in strikethroughSpans) {
                            spannableString.removeSpan(strikethroughSpan)
                        }
                    }

                    taskTitle.text = spannableString

                    true
                }
            }
        }
    }
}