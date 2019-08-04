package com.tsquaredapplications.agua.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = DrinkType::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("drinkTypeId")
    )]
)
class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val day: String, // mm-dd-yyyy,
    val time: Long,
    val drinkTypeId: Int,
    val amount: Int
)