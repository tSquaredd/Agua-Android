package com.tsquaredapplications.agua.ext

import com.tsquaredapplications.agua.model.Entry

fun Entry.update(amount: Int? = null, drinkTypeId: Int? = null, time: Long? = null): Entry =
    Entry(
        id = this.id,
        day = this.day,
        time = time ?: this.time,
        amount = amount ?: this.amount,
        drinkTypeId = drinkTypeId ?: this.drinkTypeId
    )