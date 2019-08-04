package com.tsquaredapplications.agua.ext

import com.tsquaredapplications.agua.model.Entry
import java.util.*

fun Calendar.createEntryWith(amount: Int, drinkTypeId: Int): Entry =
    Entry(
        id = 0,
        day = "${this.get(Calendar.MONTH)}/${this.get(Calendar.DAY_OF_MONTH)}/${this.get(Calendar.YEAR)}",
        time = this.timeInMillis,
        drinkTypeId = drinkTypeId,
        amount = amount
    )

fun createFormattedDateStringFor(month: Int, day: Int, year: Int) = "$month/$day/$year"