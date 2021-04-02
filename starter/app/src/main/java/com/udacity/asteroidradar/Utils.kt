package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {
        fun convertDateStringToFormattedString(date: Date, format: String, locale: Locale = Locale.getDefault()) : String {
            val formatter = SimpleDateFormat(format, locale)
            return formatter.format(date)
        }
    }

}