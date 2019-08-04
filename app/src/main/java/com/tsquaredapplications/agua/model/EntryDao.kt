package com.tsquaredapplications.agua.model

import androidx.room.*

@Dao
interface EntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntry(entry: Entry)

    @Update
    fun updateEntry(entry: Entry)

    @Delete
    fun deleteEntry(entry: Entry)

    @Query("DELETE FROM entry WHERE day == :formattedDate")
    fun deleteEntriesForDay(formattedDate: String)

    @Query("SELECT * FROM entry WHERE day == :formattedDate ORDER BY time ASC")
    fun getEntriesForDay(formattedDate: String): Array<Entry>

    @Query("SELECT * FROM entry WHERE drinkTypeId == :drinkTypeId")
    fun getEntriesForDrinkType(drinkTypeId: Int): Array<Entry>
}