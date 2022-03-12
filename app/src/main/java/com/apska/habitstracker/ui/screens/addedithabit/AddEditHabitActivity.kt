package com.apska.habitstracker.ui.screens.addedithabit

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.ActivityAddEditHabitBinding
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.model.HabitType
import com.apska.habitstracker.ui.view.colorpicker.ColorPicker
import com.apska.habitstracker.ui.screens.FieldValidator
import com.google.android.material.textfield.TextInputLayout

class AddEditHabitActivity : AppCompatActivity() {

    companion object {
        private const val KEY_SELECTED_PRIORITY_INDEX = "selected_priority_index"
        private const val KEY_SELECTED_TYPE_INDEX = "selected_type_index"
        const val EXTRA_HABIT = "habit"

        fun getIntent(context: Context, habit: Habit? = null): Intent {
            return Intent(context, AddEditHabitActivity::class.java).also {
                if (habit != null) {
                    it.putExtra(EXTRA_HABIT, habit)
                }
            }
        }
    }

    private lateinit var binding: ActivityAddEditHabitBinding
    private lateinit var priorityEditText: AppCompatAutoCompleteTextView
    private var selectedPriorityIndex = -1
    private var selectedTypeIndex = -1
    private var selectedColorFromPicker = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditHabitBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val priorityAdapter = ArrayAdapter(this,
            R.layout.priority_list_item, HabitPriority.values().map { habitPriority ->
                habitPriority.getTextValue(this)
            })

        priorityEditText = binding.priorityEditText as AppCompatAutoCompleteTextView

        priorityEditText.apply {
            setAdapter(priorityAdapter)
            setOnItemClickListener { _, _, position, _ ->
                selectedPriorityIndex = position
            }
        }

        HabitType.values().forEach { habitType ->
            val radioButton = RadioButton(this)
            radioButton.id = habitType.ordinal
            radioButton.text = habitType.getTextValue(this)
            radioButton.setTextColor(ContextCompat.getColor(this, R.color.primaryTextColor))

            if (habitType.ordinal == selectedTypeIndex) {
                radioButton.isChecked = true
            }

            binding.habitTypeRadioGroup.addView(radioButton)
        }

        binding.habitTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedTypeIndex = checkedId
        }

        if (intent.hasExtra(EXTRA_HABIT)) {
            title = getText(R.string.header_edit)

            val habit: Habit = intent.getParcelableExtra(EXTRA_HABIT)
                ?: throw Exception("Не удалось получить привычку из переданного Intent.")

            selectedPriorityIndex = habit.priority.ordinal
            priorityEditText.setText(habit.priority.getTextValue(this), false)



            binding.apply {
                headerEditText.setText(habit.header)
                descriptionEditText.setText(habit.description)
                executeCountEditText.setText(habit.executeCount.toString())
                periodEditText.setText(habit.period)
                (habitTypeRadioGroup.getChildAt(habit.type.ordinal) as RadioButton).isChecked = true
                selectedColorPickerView.canvasBackgroundColor = habit.color
                selectedColorPickerView.visibility = View.VISIBLE
                selectedColorFromPicker = habit.color
                selectedColorTextView.text = getText(R.string.habit_color_selected_label)
            }

        } else {
            title = getText(R.string.header_add)

            (binding.habitTypeRadioGroup.getChildAt(HabitType.NEUTRAL.ordinal) as RadioButton).isChecked =
                true
        }

        binding.saveButton.setOnClickListener {
            if (!isFieldsValid()) {
                return@setOnClickListener
            }

            val newHabit = Habit(
                header = binding.headerEditText.text.toString(),
                description = binding.descriptionEditText.text.toString(),
                priority = HabitPriority.values()[selectedPriorityIndex],
                type = HabitType.values()[selectedTypeIndex],
                executeCount = binding.executeCountEditText.text.toString().toInt(),
                period = binding.periodEditText.text.toString(),
                color = selectedColorFromPicker
            )

            val intent = Intent()
            intent.putExtra(EXTRA_HABIT, newHabit)
            setResult(RESULT_OK, intent)
            finish()
        }

        setupValidatorListeners()

        ColorPicker(this, binding.colorsPickerView, binding.rootLinearLayout).apply {
            this.selectedColor = selectedColorFromPicker

            onColorClickListener = ColorPicker.OnColorClickListener { colorPickerView ->
                selectedColorFromPicker = colorPickerView.canvasBackgroundColor
                binding.selectedColorPickerView.canvasBackgroundColor = selectedColor
                binding.selectedColorTextView.text = getText(R.string.habit_color_selected_label)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        priorityEditText.setText("",false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_PRIORITY_INDEX, selectedPriorityIndex)
        outState.putInt(KEY_SELECTED_TYPE_INDEX, selectedTypeIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        selectedPriorityIndex = savedInstanceState.getInt(KEY_SELECTED_PRIORITY_INDEX)
        selectedTypeIndex = savedInstanceState.getInt(KEY_SELECTED_TYPE_INDEX)

        if (selectedPriorityIndex != -1) {
            priorityEditText.setText(HabitPriority.values()[selectedPriorityIndex].getTextValue(this),
                false)
        }
    }

    private fun isFieldsValid(): Boolean {
        val isHeaderValidated = validateHeader()
        val isDescriptionValidated = validateDescription()
        val isExecuteCountValidated = validateExecuteCount()
        val isPeriodValidated = validatePeriod()
        val isPriorityValidated = validatePriority()

        return isHeaderValidated && isDescriptionValidated && isExecuteCountValidated &&
                isPeriodValidated && isPriorityValidated
    }

    private fun setupValidatorListeners() {
        binding.headerEditText.addTextChangedListener(
            AddEditFieldValidator(object : FieldValidator {
                override fun validate() {
                    validateHeader()
                }
            }))

        binding.descriptionEditText.addTextChangedListener(
            AddEditFieldValidator(object : FieldValidator {
                override fun validate() {
                    validateDescription()
                }
            }))

        binding.executeCountEditText.addTextChangedListener(
            AddEditFieldValidator(object : FieldValidator {
                override fun validate() {
                    validateExecuteCount()
                }
            }))

        binding.periodEditText.addTextChangedListener(
            AddEditFieldValidator(object : FieldValidator {
                override fun validate() {
                    validatePeriod()
                }
            }))

        binding.priorityEditText.addTextChangedListener(
            AddEditFieldValidator(object : FieldValidator {
                override fun validate() {
                    validatePriority()
                }
            }))

    }

    private fun validateEmptyField(textInputLayout: TextInputLayout, editText: EditText) =
        if (editText.text.toString().trim().isEmpty()) {
            textInputLayout.error = getText(R.string.required_field_err)
            false
        } else {
            textInputLayout.error = null
            true
        }

    private fun validateHeader() =
        validateEmptyField(binding.headerTextInputLayout, binding.headerEditText)

    private fun validateDescription() =
        validateEmptyField(binding.descriptionTextInputLayout, binding.descriptionEditText)

    private fun validateExecuteCount() =
        validateEmptyField(binding.executeCountTextInputLayout, binding.executeCountEditText)

    private fun validatePeriod() =
        validateEmptyField(binding.periodTextInputLayout, binding.periodEditText)

    private fun validatePriority() =
        validateEmptyField(binding.priorityTextInputLayout, binding.priorityEditText)
}