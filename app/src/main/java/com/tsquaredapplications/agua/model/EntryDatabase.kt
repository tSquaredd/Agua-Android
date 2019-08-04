package com.tsquaredapplications.agua.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Entry::class, DrinkType::class], version = 1)
abstract class EntryDatabase: RoomDatabase(){
    abstract fun entryDao(): EntryDao

    abstract fun drinkTypeDao(): DrinkTypeDao
}