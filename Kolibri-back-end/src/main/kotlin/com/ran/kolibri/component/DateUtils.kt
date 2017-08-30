package com.ran.kolibri.component

import org.springframework.stereotype.Component
import java.util.*

@Component
class DateUtils {

    companion object {
        private val DAY_MILLISECONDS = 24 * 60 * 60 * 1000
    }

    fun getDateWithoutTime(date: Date): Date {
        val time = date.time - (date.time % DAY_MILLISECONDS)
        return Date(time)
    }

}
