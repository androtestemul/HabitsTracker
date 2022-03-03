package com.apska.habitstracker.ui.screens.habitslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.ActivityAddEditHabitBinding
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.model.HabitType
import com.apska.habitstracker.ui.screens.FieldValidator
import com.google.android.material.textfield.TextInputLayout

class AddEditHabitActivity : AppCompatActivity() {

    companion object {
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
    private var selectedPriorityIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditHabitBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val habit: Habit?

        if (intent.hasExtra(EXTRA_HABIT)) {
            title = getText(R.string.header_edit)
            habit = intent.getParcelableExtra(EXTRA_HABIT)
        } else {
            title = getText(R.string.header_add)
            habit = null
        }

        val priorityAdapter = ArrayAdapter(this,
            R.layout.priority_list_item, HabitPriority.values().map { habitPriority ->
                habitPriority.getTextValue(this)
            })

        val priorityEditText = binding.priorityEditText as? AutoCompleteTextView

        priorityEditText?.apply {
            setAdapter(priorityAdapter)
            setOnItemClickListener { _, _, position, _ ->
                selectedPriorityIndex = position
            }
        }

        HabitType.values().forEach { habitType ->
            val radioButton = RadioButton(this)
            radioButton.text = habitType.getTextValue(this)
            binding.habitTypeRadioGroup.addView(radioButton)
        }

        if (habit == null) {
            (binding.habitTypeRadioGroup.getChildAt(HabitType.NEUTRAL.ordinal) as RadioButton).isChecked =
                true
        }

        habit?.let {
            selectedPriorityIndex = it.priority.ordinal
            priorityEditText?.setText(it.priority.getTextValue(this), false)

            binding.apply {
                headerEditText.setText(it.header)
                descriptionEditText.setText(it.description)
                executeCountEditText.setText(it.executeCount.toString())
                periodEditText.setText(it.period)
                (binding.habitTypeRadioGroup.getChildAt(it.type.ordinal) as RadioButton).isChecked =
                    true
            }
        }

        binding.saveButton.setOnClickListener {
            if (!isFieldsValid()) {
                return@setOnClickListener
            }

            var type: HabitType? = null

            for (i in 0 until binding.habitTypeRadioGroup.size - 1) {
                if ((binding.habitTypeRadioGroup.getChildAt(i) as RadioButton).isChecked) {
                    type = HabitType.values()[i]
                    break
                }
            }

            if (type == null) {
                throw Exception("Не определен тип привычки!")
            }

            val newHabit = Habit(
                header = binding.headerEditText.text.toString(),
                description = binding.descriptionEditText.text.toString(),
                priority = HabitPriority.values()[selectedPriorityIndex],
                type = type,
                executeCount = binding.executeCountEditText.text.toString().toInt(),
                period = binding.periodEditText.text.toString()
            )

            val intent = Intent()
            intent.putExtra(EXTRA_HABIT, newHabit)
            setResult(RESULT_OK, intent)
            finish()
        }

        setupValidatorListeners()
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