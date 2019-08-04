package com.tsquaredapplications.agua.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface  DrinkTypeDao {

    @Insert
    fun insert(drinkType: DrinkType)

    @Update
    fun update(drinkType: DrinkType)

    @Query("SELECT * FROM drinktype")
    fun getAllDrinkTypes(): Array<DrinkType>
}