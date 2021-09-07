package com.warleysr.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.warleysr.todolist.databinding.ActivityAddTaskBinding
import com.warleysr.todolist.datasource.TaskDataSource
import com.warleysr.todolist.extensions.format
import com.warleysr.todolist.extensions.text
import com.warleysr.todolist.model.Task
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.title.text = it.title
                binding.date.text = it.date
                binding.hour.text = it.hour
            }

        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.date.editText?.setOnClickListener() {
            val datepicker = MaterialDatePicker.Builder.datePicker().build()

            datepicker.addOnPositiveButtonClickListener {
                val timezone = TimeZone.getDefault()
                val offset = timezone.getOffset(Date().time) * -1
                binding.date.text = Date(it + offset).format()
            }

            datepicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.hour.editText?.setOnClickListener() {
            val timepicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timepicker.addOnPositiveButtonClickListener() {
                val hour = if (timepicker.hour in 0..9) "0${timepicker.hour}" else timepicker.hour
                val minute = if (timepicker.minute in 0..9) "0${timepicker.minute}" else timepicker.minute
                binding.hour.text = "$hour:$minute"
            }
            timepicker.show(supportFragmentManager, null)
        }

        binding.create.setOnClickListener() {
            val t = Task(
                    title = binding.title.text,
                    date = binding.date.text,
                    hour = binding.hour.text,
                    id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(t)
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.cancel.setOnClickListener() {
            finish()
        }

    }

    companion object {
        const val TASK_ID = "task_id"
    }
}