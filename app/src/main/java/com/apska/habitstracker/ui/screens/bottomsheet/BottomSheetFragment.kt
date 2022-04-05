package com.apska.habitstracker.ui.screens.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.fragment.app.activityViewModels
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.FragmentBottomSheetBinding
import com.apska.habitstracker.databinding.FragmentHabitsListBinding
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.ui.screens.habitpager.HabitPagerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding
        get() = _binding!!

    private val habitPagerViewModel : HabitPagerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)


        binding.headerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                habitPagerViewModel.searchHeader = p0.toString()
            }

        }) /*{ _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                habitPagerViewModel.searchHeader = binding.headerEditText.text.toString()
                true
            } else {
                false
            }
        }*/

        val priorityAdapter = ArrayAdapter(requireContext(),
            R.layout.priority_list_item, HabitPriority.values().map { habitPriority ->
                habitPriority.getTextValue(requireContext())
            })

        (binding.priorityEditText as AppCompatAutoCompleteTextView).apply {
            setAdapter(priorityAdapter)

            setOnItemClickListener { _, _, position, _ ->
                habitPagerViewModel.selectedPriority = HabitPriority.values()[position]
            }

            habitPagerViewModel.selectedPriority?.let {
                setText(it.getTextValue(requireContext()), false)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()


        habitPagerViewModel.selectedPriority?.let {
            binding.priorityEditText.setText(it.getTextValue(requireContext()), false)
        }
    }

    override fun onPause() {
        super.onPause()

        binding.priorityEditText.setText("", false)

    }
}