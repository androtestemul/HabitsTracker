package com.apska.habitstracker.ui.screens.addedithabit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.FragmentAddEditHabitBinding
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.model.HabitType
import com.apska.habitstracker.ui.screens.FieldValidator
import com.apska.habitstracker.ui.view.colorpicker.ColorPicker
import com.apska.habitstracker.ui.view.colorview.ColorView
import com.google.android.material.textfield.TextInputLayout

class AddEditHabitFragment : Fragment() {

    private var _binding: FragmentAddEditHabitBinding? = null
    private val binding
        get() = _binding!!

    private val args: AddEditHabitFragmentArgs by navArgs()
    private val addEditHabitViewModel: AddEditHabitViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditHabitBinding.inflate(inflater, container, false)

        val priorityAdapter = ArrayAdapter(requireContext(),
            R.layout.priority_list_item, HabitPriority.values().map { habitPriority ->
                habitPriority.getTextValue(requireContext())
            })

        (binding.priorityEditText as AppCompatAutoCompleteTextView).apply {
            setAdapter(priorityAdapter)

            setOnItemClickListener { _, _, position, _ ->
                addEditHabitViewModel.selectedPriority = HabitPriority.values()[position]
            }

            addEditHabitViewModel.selectedPriority?.let {
                setText(it.getTextValue(requireContext()), false)
            }
        }

        binding.selectedColorPickerView.canvasBackgroundColor =
            addEditHabitViewModel.selectedColorFromPicker

        HabitType.values().forEach { habitType ->
            val radioButton = RadioButton(requireContext())
            radioButton.id = habitType.ordinal
            radioButton.text = habitType.getTextValue(requireContext())
            radioButton.setTextColor(ContextCompat.getColor(requireContext(),
                R.color.primaryTextColor))

            if (habitType == addEditHabitViewModel.selectedType) {
                radioButton.isChecked = true
            }

            binding.habitTypeRadioGroup.addView(radioButton)
        }

        binding.habitTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            addEditHabitViewModel.selectedType = HabitType.values()[checkedId]
        }

        val habitId = args.habitId

        if (habitId != -1L) {
            val habit = addEditHabitViewModel.getHabit(habitId) ?: throw Exception("Привычка не найдена")

            addEditHabitViewModel.selectedPriority = habit.priority

            binding.apply {
                headerEditText.setText(habit.header)
                descriptionEditText.setText(habit.description)
                priorityEditText.setText(habit.priority.getTextValue(requireContext()), false)
                executeCountEditText.setText(habit.executeCount.toString())
                periodEditText.setText(habit.period)
                (habitTypeRadioGroup.getChildAt(habit.type.ordinal) as RadioButton).isChecked = true

                if (habit.color != ColorView.DEFAULT_COLOR) {
                    selectedColorPickerView.canvasBackgroundColor = habit.color
                    addEditHabitViewModel.selectedColorFromPicker = habit.color
                    selectedColorTextView.text = getText(R.string.habit_color_selected_label)
                }
            }
        } else {
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
                priority = addEditHabitViewModel.selectedPriority
                    ?: throw Exception("Priority of new Habit is null"),
                type = addEditHabitViewModel.selectedType
                    ?: throw Exception("Type of new Habit is null"),
                executeCount = binding.executeCountEditText.text.toString().toInt(),
                period = binding.periodEditText.text.toString(),
                color = addEditHabitViewModel.selectedColorFromPicker
            )

            if (habitId == -1L) {
                addEditHabitViewModel.addHabit(newHabit)
            } else {
                addEditHabitViewModel.updateHabit(newHabit)
            }

            findNavController().popBackStack()
        }

        setupValidatorListeners()

        ColorPicker(requireActivity(),
            binding.colorsPickerView,
            binding.rootLinearLayout).apply {
            this.selectedColor = addEditHabitViewModel.selectedColorFromPicker

            onColorClickListener = ColorPicker.OnColorClickListener { colorPickerView ->
                addEditHabitViewModel.selectedColorFromPicker =
                    colorPickerView.canvasBackgroundColor

                binding.apply {
                    selectedColorPickerView.canvasBackgroundColor = selectedColor

                    //устанавливается только 1 раз
                    if (rgbLabelTextView.visibility != View.VISIBLE) {
                        selectedColorTextView.text = getText(R.string.habit_color_selected_label)
                        rgbLabelTextView.visibility = View.VISIBLE
                        rgbValueTextView.visibility = View.VISIBLE
                        hsvLabelTextView.visibility = View.VISIBLE
                        hsvValueTextView.visibility = View.VISIBLE
                    }

                    rgbValueTextView.text = selectedColorAsRgb()
                    hsvValueTextView.text = selectedColorAsHsv()
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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