package com.estholon.alarmapp.providers

import android.content.Context
import com.estholon.alarmapp.R

class RepetitionTypesProvider {

    companion object {

        fun getRepetitionTypes(context: Context) : MutableList<String> {
            return mutableListOf(
                context.getString(R.string.seconds),
                context.getString(R.string.minutes),
                context.getString(R.string.hours),
                context.getString(R.string.días),
            )
        }

        fun getRepetitionTypesText(context: Context) : List<String> {
            return getRepetitionTypes(context).map { it }
        }

    }

}