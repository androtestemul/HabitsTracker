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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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

    companion object {
        private const val KEY_SELECTED_PRIORITY_INDEX = "selected_priority_index"
        private const val KEY_SELECTED_TYPE_INDEX = "selected_type_index"
        private const val KEY_SELECTED_COLOR_FROM_PICKER = "selected_color_from_picker"

        const val REQUEST_KEY_ADD_HABIT = "request_key_add_habit"
        const val REQUEST_KEY_EDIT_HABIT = "request_key_edit_habit"
        const val EXTRA_HABIT = "extra_habit"

        /*fun newInstance(habit: Habit? = null): AddEditHabitFragment {
            val fragment = AddEditHabitFragment()

            habit?.let {
                val bundle = Bundle()
                bundle.putParcelable(ARG_HABIT, it)
                fragment.arguments = bundle
        }

            return fragment
        }*/
    }

    private var _binding: FragmentAddEditHabitBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var priorityEditText: AppCompatAutoCompleteTextView
    private var selectedPriorityIndex = -1
    private var selectedTypeIndex = -1
    private var selectedColorFromPicker = ColorView.DEFAULT_COLOR
    private lateinit var colorPicker: ColorPicker
    private val args: AddEditHabitFragmentArgs by navArgs()

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

        priorityEditText = binding.priorityEditText as AppCompatAutoCompleteTextView

        if (savedInstanceState != null) {
            selectedPriorityIndex = savedInstanceState.getInt(KEY_SELECTED_PRIORITY_INDEX)
            selectedTypeIndex = savedInstanceState.getInt(KEY_SELECTED_TYPE_INDEX)
            selectedColorFromPicker = savedInstanceState.getInt(KEY_SELECTED_COLOR_FROM_PICKER)

            if (selectedPriorityIndex != -1) {
                priorityEditText.setText(HabitPriority.values()[selectedPriorityIndex]
                    .getTextValue(requireContext()), false)
            }

            binding.selectedColorPickerView.canvasBackgroundColor = selectedColorFromPicker
        }

        priorityEditText.apply {
            setAdapter(priorityAdapter)
            setOnItemClickListener { _, _, position, _ ->
                selectedPriorityIndex = position
            }
        }

        HabitType.values().forEach { habitType ->
            val radioButton = RadioButton(requireContext())
            radioButton.id = habitType.ordinal
            radioButton.text = habitType.getTextValue(requireContext())
            radioButton.setTextColor(ContextCompat.getColor(requireContext(),
                R.color.primaryTextColor))

            if (habitType.ordinal == selectedTypeIndex) {
                radioButton.isChecked = true
            }

            binding.habitTypeRadioGroup.addView(radioButton)
        }

        binding.habitTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedTypeIndex = checkedId
        }

        val habit = args.habit

        if (habit != null) {
            selectedPriorityIndex = habit.priority.ordinal
            priorityEditText.setText(habit.priority.getTextValue(requireContext()), false)

            binding.apply {
                headerEditText.setText(habit.header)
                descriptionEditText.setText(habit.description)
                executeCountEditText.setText(habit.executeCount.toString())
                periodEditText.setText(habit.period)
                (habitTypeRadioGroup.getChildAt(habit.type.ordinal) as RadioButton).isChecked = true

                if (habit.color != ColorView.DEFAULT_COLOR) {
                    selectedColorPickerView.canvasBackgroundColor = habit.color
                    selectedColorFromPicker = habit.color
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
                priority = HabitPriority.values()[selectedPriorityIndex],
                type = HabitType.values()[selectedTypeIndex],
                executeCount = binding.executeCountEditText.text.toString().toInt(),
                period = binding.periodEditText.text.toString(),
                color = selectedColorFromPicker
            )

            val key = if (habit == null) REQUEST_KEY_ADD_HABIT else REQUEST_KEY_EDIT_HABIT

            parentFragmentManager.setFragmentResult(key, bundleOf(EXTRA_HABIT to newHabit))
            findNavController().popBackStack()
        }

        setupValidatorListeners()

        colorPicker = ColorPicker(requireActivity(),
            binding.colorsPickerView,
            binding.rootLinearLayout).apply {
            this.selectedColor = selectedColorFromPicker

            onColorClickListener = ColorPicker.OnColorClickListener { colorPickerView ->
                selectedColorFromPicker = colorPickerView.canvasBackgroundColor

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


    override fun onStop() {
        super.onStop()
        priorityEditText.setText("", false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_PRIORITY_INDEX, selectedPriorityIndex)
        outState.putInt(KEY_SELECTED_TYPE_INDEX, selectedTypeIndex)
        outState.putInt(KEY_SELECTED_COLOR_FROM_PICKER, selectedColorFromPicker)
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