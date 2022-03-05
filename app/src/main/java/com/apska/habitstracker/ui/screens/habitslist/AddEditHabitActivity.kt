package com.apska.habitstracker.ui.screens.habitslist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Insets
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import androidx.core.view.size
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.ActivityAddEditHabitBinding
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.model.HabitType
import com.apska.habitstracker.ui.ColorPickerView
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
    private var selectedColor = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditHabitBinding.inflate(layoutInflater)

        setContentView(binding.root)

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

        if (intent.hasExtra(EXTRA_HABIT)) {
            title = getText(R.string.header_edit)

            val habit: Habit = intent.getParcelableExtra(EXTRA_HABIT)
                ?: throw Exception("Не удалось получить привычку из переданного Intent.")

            selectedPriorityIndex = habit.priority.ordinal
            priorityEditText?.setText(habit.priority.getTextValue(this), false)

            binding.apply {
                headerEditText.setText(habit.header)
                descriptionEditText.setText(habit.description)
                executeCountEditText.setText(habit.executeCount.toString())
                periodEditText.setText(habit.period)
                (habitTypeRadioGroup.getChildAt(habit.type.ordinal) as RadioButton).isChecked = true
                selectedColorPickerView.canvasBackgroundColor = habit.color
                selectedColorPickerView.visibility = View.VISIBLE
                selectedColor = habit.color
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

            var type: HabitType? = null

            for (i in 0..binding.habitTypeRadioGroup.size) {
                if ((binding.habitTypeRadioGroup.getChildAt(i) as RadioButton).isChecked) {
                    type = HabitType.values()[i]
                    break
                }
            }

            if (type == null) {
                type = HabitType.values()[HabitType.values().lastIndex]
            }

            val newHabit = Habit(
                header = binding.headerEditText.text.toString(),
                description = binding.descriptionEditText.text.toString(),
                priority = HabitPriority.values()[selectedPriorityIndex],
                type = type,
                executeCount = binding.executeCountEditText.text.toString().toInt(),
                period = binding.periodEditText.text.toString(),
                color = selectedColor
            )

            val intent = Intent()
            intent.putExtra(EXTRA_HABIT, newHabit)
            setResult(RESULT_OK, intent)
            finish()
        }

        setupValidatorListeners()

        buildColorPicker()

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

    private fun getScreenWidth(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    private fun buildColorPicker() {
        val squareTotalCount = 16
        val startDegree: Float = 360 / squareTotalCount.toFloat() / 2

        val intColors = ArrayList<Int>(16)

        for (i in 0 until squareTotalCount) {
            val degree = startDegree + (startDegree * 2 * i)
            val hueColor = Color.HSVToColor(floatArrayOf(degree, 100f, 100f))
            intColors.add(hueColor)
        }

        val drawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.RED, *intColors.toIntArray(), Color.RED))

        drawable.shape = GradientDrawable.RECTANGLE
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT

        binding.colorsPickerView.background = drawable

        val squareOnScreenCount = 4
        //val squareMarginWidthPercent = 25
        val screenWidth =
            getScreenWidth(this) - binding.rootLinearLayout.paddingLeft - binding.rootLinearLayout.paddingRight
        //val squareFullWidth: Int = screenWidth/squareOnScreenCount

        val squareMarginWidth: Int = screenWidth / squareOnScreenCount / 6
        //val squareMarginWidth: Int = squareFullWidth * squareMarginWidthPercent/100

        val squareWidth: Int = squareMarginWidth * 4
        //val squareWidth: Int = squareMarginWidth*4


        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(squareMarginWidth)
        layoutParams.width = squareWidth
        layoutParams.height = squareWidth


        for (i in 0 until squareTotalCount) {
            val squareView = ColorPickerView(this, layoutParams, intColors[i])

            if (squareView.canvasBackgroundColor == selectedColor) {
                squareView.isViewSelected = true
            }

            squareView.setOnClickListener {
                val clickedSquareView = it as ColorPickerView

                if (clickedSquareView.isViewSelected) {
                    return@setOnClickListener
                }

                //Снимаем флаг isViewSelected с предыдущей выбранной View
                if (selectedColor != Color.WHITE) {
                    (binding.colorsPickerView
                        .getChildAt(intColors.indexOf(selectedColor)) as ColorPickerView).isViewSelected =
                        false
                }

                selectedColor = clickedSquareView.canvasBackgroundColor
                clickedSquareView.isViewSelected = true

                binding.selectedColorPickerView.canvasBackgroundColor = selectedColor
                binding.selectedColorTextView.text = getText(R.string.habit_color_selected_label)
            }

            binding.colorsPickerView.addView(squareView)
        }
    }
}