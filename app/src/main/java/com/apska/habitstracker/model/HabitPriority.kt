package com.apska.habitstracker.model

import android.content.Context
import android.os.Parcelable
import com.apska.habitstracker.R
import kotlinx.parcelize.Parcelize


@Parcelize
enum class HabitPriority : Parcelable {
    HIGH {
        val priority = 1000
        override val resourceId = R.string.habit_priority_high
    },

    MIDDLE {
        val priority = 500
        override val resourceId = R.string.habit_priority_middle
    },

    LOW {
        val priority: Int = 1
        override val resourceId = R.string.habit_priority_low
    };

    abstract val resourceId: Int

    fun getTextValue(context: Context) = context.getString(this.resourceId)
}