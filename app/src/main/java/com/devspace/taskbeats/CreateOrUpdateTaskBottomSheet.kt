package com.devspace.taskbeats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class CreateOrUpdateTaskBottomSheet(
    private val categoryList: List<CategoryEntity>,
    private val task: TaskUiData? = null,
    private val onCreateClicked: (TaskUiData) -> Unit,
    private val onUpdateClicked: (TaskUiData) -> Unit,
    private val onDeleteClicked: (TaskUiData) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_or_update_task_bottom_sheet, container, false)
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val btnCreateOrUpdate = view.findViewById<Button>(R.id.btn_task_create_or_update)
        val btnDelete = view.findViewById<Button>(R.id.btn_task_delete)
        val tieTaskName = view.findViewById<TextInputEditText>(R.id.tie_task_name)
        val spinner: Spinner = view.findViewById(R.id.spinner_category_list)
        var taskCategory: String? = null
        val categoryListTemp = mutableListOf("Select a category")
        categoryListTemp.addAll(categoryList.map { it.name })
        val categoryStr: List<String> = categoryListTemp

        // Spinner Adapter setup
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categoryStr
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Spinner Item Selected Listener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                taskCategory = categoryStr[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // Set the BottomSheet title and button text for create or update mode
        if (task == null) {
            btnDelete.visibility = View.GONE
            tvTitle.setText(R.string.create_task_title)
            btnCreateOrUpdate.setText(R.string.create)
        } else {
            tvTitle.setText(R.string.update_task_tittle)
            btnCreateOrUpdate.setText(R.string.update)
            tieTaskName.setText(task.name)
            btnDelete.visibility = View.VISIBLE

            // Set the spinner's selection to the task's current category
            val currentCategory = categoryList.find { it.name == task.category }
            currentCategory?.let {
                val index = categoryList.indexOf(it)
                spinner.setSelection(index)
            }
        }

        // Delete button click listener
        btnDelete.setOnClickListener {
            if (task != null) {
                onDeleteClicked.invoke(task)
                dismiss()
            } else {
            }
        }

        // Create/Update button click listener
            btnCreateOrUpdate.setOnClickListener {
                val name = tieTaskName.text.toString().trim()
                if (taskCategory != "Select a category" && name.isNotEmpty()) {
                    if (task == null) {
                        onCreateClicked.invoke(
                            TaskUiData(
                                id = 0,
                                name = name,
                                category = requireNotNull(taskCategory)
                            )
                        )
                    } else {
                        onUpdateClicked.invoke(
                            TaskUiData(
                                id = task.id,
                                name = name,
                                category = requireNotNull(taskCategory)
                            )
                        )
                    }
                    dismiss()
                } else {
                    Snackbar.make(btnCreateOrUpdate, "Select a category", Snackbar.LENGTH_SHORT).show()
                }
            }
            return view
        }
    }