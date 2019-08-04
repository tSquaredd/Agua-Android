package com.tsquaredapplications.agua.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DrinkType(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val color: String,
    val iconResource: String
)