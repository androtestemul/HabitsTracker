package com.apska.habitstracker.ui.screens.addedithabit

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.FragmentAddEditHabitBinding
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.model.HabitType
import com.apska.habitstracker.ui.view.colorpicker.ColorPicker
import com.apska.habitstracker.ui.view.colorview.ColorView

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
            addEditHabitViewModel.getHabit(habitId)
        } else {
            (binding.habitTypeRadioGroup.getChildAt(HabitType.NEUTRAL.ordinal) as RadioButton).isChecked =
                true
        }

        binding.saveButton.setOnClickListener {
            addEditHabitViewModel.saveHabit(
                    id = habitId,
                    header = binding.headerEditText.text.toString(),
                    description = binding.descriptionEditText.text.toString(),
                    executeCount = binding.executeCountEditText.text.toString(),
                    period = binding.periodEditText.text.toString()
                )
        }

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

        addEditHabitViewModel.habit.observe(viewLifecycleOwner) { habit ->
            habit?.let {
                binding.apply {
                    headerEditText.setText(habit.header)
                    descriptionEditText.setText(habit.description)
                    priorityEditText.setText(habit.priority.getTextValue(requireContext()), false)
                    executeCountEditText.setText(habit.executeCount.toString())
                    periodEditText.setText(habit.period)
                    (habitTypeRadioGroup.getChildAt(habit.type.ordinal) as RadioButton).isChecked = true

                    if (habit.color != ColorView.DEFAULT_COLOR) {
                        selectedColorPickerView.canvasBackgroundColor = habit.color

                        selectedColorTextView.text = getText(R.string.habit_color_selected_label)
                    }
                }
            }
        }

        addEditHabitViewModel.validateResult.observe(viewLifecycleOwner) { validateResult ->
            validateResult ?: return@observe

            if (!validateResult.isValid) {
                validateForm(validateResult.validatedFields)
            }
        }

        addEditHabitViewModel.processResult.observe(viewLifecycleOwner) { saveResult ->
            saveResult ?: return@observe

            when (saveResult) {
                is ProcessResult.SAVED -> findNavController().popBackStack()
                is ProcessResult.LOADED -> setProgressVisibility(false)
                is ProcessResult.ERROR -> {
                    setProgressVisibility(false)

                    val errorDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogApp)
                        .setMessage(saveResult.message ?: getString(R.string.error_has_occurred))
                        .setTitle(R.string.dialog_header)
                        .create()

                    if (saveResult.formErrorType == FormError.LOAD) {
                        errorDialog.apply {
                            setButton(Dialog.BUTTON_POSITIVE, getText(R.string.dialog_btn_ok)) { _, _ ->
                                findNavController().popBackStack()
                            }

                            setCancelable(false)
                        }
                    }

                    errorDialog.show()
                }
                is ProcessResult.PROCESSING -> setProgressVisibility(true)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setProgressVisibility(show: Boolean) {
        if (show){
            binding.progressOverlay.visibility = View.VISIBLE
        } else {
            binding.progressOverlay.visibility = View.GONE
        }
    }

    private fun validateForm(validatedFields: HashMap<ValidatedFields, ValidationErrorTypes>) {
        ValidatedFields.values().forEach { validatedField ->
            when (validatedField) {
                ValidatedFields.HEADER -> {
                    if (validatedFields.containsKey(ValidatedFields.HEADER)) {
                        if (validatedFields[ValidatedFields.HEADER] == ValidationErrorTypes.EMPTY) {
                            binding.headerTextInputLayout.error = getText(R.string.required_field_err)
                        }
                    } else {
                        binding.headerTextInputLayout.error = null
                    }
                }

                ValidatedFields.DESCRIPTION -> {
                    if (validatedFields.containsKey(ValidatedFields.DESCRIPTION)) {
                        if (validatedFields[ValidatedFields.DESCRIPTION] == ValidationErrorTypes.EMPTY) {
                            binding.descriptionTextInputLayout.error = getText(R.string.required_field_err)
                        }
                    } else {
                        binding.descriptionTextInputLayout.error = null
                    }
                }

                ValidatedFields.PRIORITY -> {
                    if (validatedFields.containsKey(ValidatedFields.PRIORITY)) {
                        if (validatedFields[ValidatedFields.PRIORITY] == ValidationErrorTypes.EMPTY) {
                            binding.priorityTextInputLayout.error = getText(R.string.required_field_err)
                        }
                    } else {
                        binding.priorityTextInputLayout.error = null
                    }
                }

                ValidatedFields.EXECUTE_COUNT -> {
                    if (validatedFields.containsKey(ValidatedFields.EXECUTE_COUNT)) {
                        if (validatedFields[ValidatedFields.EXECUTE_COUNT] == ValidationErrorTypes.EMPTY) {
                            binding.executeCountTextInputLayout.error = getText(R.string.required_field_err)
                        }
                    } else {
                        binding.executeCountTextInputLayout.error = null
                    }
                }

                ValidatedFields.PERIOD -> {
                    if (validatedFields.containsKey(ValidatedFields.PERIOD)) {
                        if (validatedFields[ValidatedFields.PERIOD] == ValidationErrorTypes.EMPTY) {
                            binding.periodTextInputLayout.error = getText(R.string.required_field_err)
                        }
                    } else {
                        binding.periodTextInputLayout.error = null
                    }
                }

                else -> {}
            }
        }
    }
}