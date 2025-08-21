package com.apska.habitstracker.presentation.ui.screens.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.apska.habitstracker.domain.model.HabitPriority
import com.apska.habitstracker.domain.model.HabitSort
import com.apska.habitstracker.presentation.R
import com.apska.habitstracker.presentation.databinding.FragmentBottomSheetBinding
import com.apska.habitstracker.presentation.di.AppComponentProvider
import com.apska.habitstracker.presentation.di.FragmentComponent
import com.apska.habitstracker.presentation.di.ViewModelFactory
import com.apska.habitstracker.presentation.getTextValue
import com.apska.habitstracker.presentation.ui.screens.habitpager.HabitPagerViewModel
import com.apska.habitstracker.presentation.ui.screens.habitpager.HabitPagerViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject
import kotlin.getValue

class BottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        private const val SORT_DIRECTION_BOUND = 60
    }

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val habitPagerViewModel : HabitPagerViewModel by viewModels { viewModelFactory }

    protected lateinit var fragmentComponent: FragmentComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Получаем FragmentComponent из AppComponent
        val appComponentProvider = requireActivity().application as AppComponentProvider
        fragmentComponent = appComponentProvider.getFragmentComponent()

        // Выполняем инжекцию
        fragmentComponent.inject(this)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        binding.headerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                habitPagerViewModel.searchHeader = p0.toString()
            }

        })

        val priorityAdapter = ArrayAdapter(requireContext(),
            R.layout.priority_list_item, HabitPriority.entries.map { habitPriority ->
                habitPriority.getTextValue(requireContext())
            })

        (binding.priorityEditText as AppCompatAutoCompleteTextView).apply {
            setAdapter(priorityAdapter)

            setOnItemClickListener { _, _, position, _ ->
                habitPagerViewModel.selectedPriority = HabitPriority.entries.get(position)
            }

            habitPagerViewModel.selectedPriority?.let {
                setText(it.getTextValue(requireContext()), false)
            }
        }

        binding.sortByPeriod.setOnClickListener {
            habitPagerViewModel.sortHabitByPeriod()
        }

        binding.resetSortFilter.setOnClickListener {
            habitPagerViewModel.resetSortAndFilter()
            binding.headerEditText.setText("")
            binding.priorityEditText.setText("", false)
        }

        habitPagerViewModel.currentSortDirection.observe(viewLifecycleOwner) { sortDirection ->
            val sortImg = when (sortDirection) {
                HabitSort.SORT_ASC -> {
                    requireContext().getDrawable(R.drawable.ic_baseline_arrow_upward_24)
                }
                HabitSort.SORT_DESC -> {
                    requireContext().getDrawable(R.drawable.ic_baseline_arrow_downward_24)
                }
                else -> {
                    null
                }
            }

            sortImg?.setBounds(0, 0, SORT_DIRECTION_BOUND, SORT_DIRECTION_BOUND)

            binding.sortByPeriod.setCompoundDrawables(null, null, sortImg, null)
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