package com.tsquaredapplications.agua

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tsquaredapplications.agua.ext.createEntryWith
import com.tsquaredapplications.agua.ext.createFormattedDateStringFor
import com.tsquaredapplications.agua.ext.update
import com.tsquaredapplications.agua.model.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class EntryTests {
    private lateinit var entryDao: EntryDao
    private lateinit var drinkTypeDao: DrinkTypeDao
    private lateinit var entryDb: EntryDatabase

    private val januaryFirstOne = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2019)
        set(Calendar.MONTH, 0)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 6)
        set(Calendar.MINUTE, 30)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private val januaryFirstTwo = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2019)
        set(Calendar.MONTH, 0)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 6)
        set(Calendar.MINUTE, 45)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private val januaryFirstThree = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2019)
        set(Calendar.MONTH, 0)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 12)
        set(Calendar.MINUTE, 45)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private val januaryThirdOne = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2019)
        set(Calendar.MONTH, 0)
        set(Calendar.DAY_OF_MONTH, 3)
        set(Calendar.HOUR_OF_DAY, 6)
        set(Calendar.MINUTE, 30)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private val januaryThirdTwo = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2019)
        set(Calendar.MONTH, 0)
        set(Calendar.DAY_OF_MONTH, 3)
        set(Calendar.HOUR_OF_DAY, 6)
        set(Calendar.MINUTE, 45)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private val januaryThirdThree = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2019)
        set(Calendar.MONTH, 0)
        set(Calendar.DAY_OF_MONTH, 3)
        set(Calendar.HOUR_OF_DAY, 12)
        set(Calendar.MINUTE, 45)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private val waterId = 1
    private val teaId = 2
    private val beerId = 3

    private val januaryFirstWater = januaryFirstOne.createEntryWith(40, waterId)
    private val januaryFirstTea = januaryFirstTwo.createEntryWith(8, teaId)
    private val januaryFirstBeer = januaryFirstThree.createEntryWith(16, beerId)

    private val januaryThirdWater = januaryThirdOne.createEntryWith(40, waterId)
    private val januaryThirdTea = januaryThirdTwo.createEntryWith(8, teaId)
    private val januaryThirdBeer = januaryThirdThree.createEntryWith(16, beerId)

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        entryDb = Room.inMemoryDatabaseBuilder(context, EntryDatabase::class.java).build()
        entryDao = entryDb.entryDao()
        drinkTypeDao = entryDb.drinkTypeDao()

        setupDrinkTypes()
    }

    private fun setupDrinkTypes() {
        drinkTypeDao.apply {
            insert(
                DrinkType(
                    id = waterId,
                    name = "Water",
                    color = "#327fa8",
                    iconResource = WATER_ICON_RESOURCE
                )
            )

            insert(
                DrinkType(
                    id = teaId,
                    name = "Tea",
                    color = "#327abc",
                    iconResource = TEA_ICON_RESOURCE
                )
            )

            insert(
                DrinkType(
                    id = beerId,
                    name = "Beer",
                    color = "#123abc",
                    iconResource = BEER_ICON_RESOURCE
                )
            )
        }
    }


    @After
    fun cleanup() {
        entryDb.close()
    }

    @Test
    fun addSingleEntry() {
        entryDao.insertEntry(januaryFirstWater)
        val actual = entryDao.getEntriesForDay(createFormattedDateStringFor(0, 1, 2019))[0]

        assertNotNull(actual)
        assertJanuaryFirstWater(actual)
    }

    @Test
    fun multipleEntries() {
        entryDao.apply {
            insertEntry(januaryFirstWater)
            insertEntry(januaryFirstTea)
            insertEntry(januaryFirstBeer)
        }

        val result = entryDao.getEntriesForDay(createFormattedDateStringFor(0, 1, 2019))
        assertEquals(3, result.size)
        assertJanuaryFirstWater(result[0])
        assertJanuaryFirstTea(result[1])
        assertJanuaryFirstBeer(result[2])
    }

    @Test
    fun multipleEntriesInsertedOutOfOrder() {
        entryDao.apply {
            insertEntry(januaryFirstTea)
            insertEntry(januaryFirstBeer)
            insertEntry(januaryFirstWater)
        }

        val result = entryDao.getEntriesForDay(createFormattedDateStringFor(0, 1, 2019))
        assertEquals(3, result.size)
        assertJanuaryFirstWater(result[0])
        assertJanuaryFirstTea(result[1])
        assertJanuaryFirstBeer(result[2])
    }

    @Test
    fun insertMultipleAndDeleteOne() {
        entryDao.apply {
            insertEntry(januaryFirstWater)
            insertEntry(januaryFirstTea)
            insertEntry(januaryFirstBeer)

            val afterEntries = getEntriesForDay(createFormattedDateStringFor(0, 1, 2019))
            deleteEntry(afterEntries[1])
        }

        val result = entryDao.getEntriesForDay(createFormattedDateStringFor(0, 1, 2019))
        assertEquals(2, result.size)
        assertJanuaryFirstWater(result[0])
        assertJanuaryFirstBeer(result[1])
    }

    @Test
    fun insertTwoDaysEntries() {
        insertTwoDays()

        var result = entryDao.getEntriesForDay(createFormattedDateStringFor(0, 1, 2019))
        assertEquals(3, result.size)
        assertJanuaryFirstWater(result[0])
        assertJanuaryFirstTea(result[1])
        assertJanuaryFirstBeer(result[2])

        result = entryDao.getEntriesForDay(createFormattedDateStringFor(0, 3, 2019))
        assertEquals(3, result.size)
        assertJanuaryThirdWater(result[0])
        assertJanuaryThirdTea(result[1])
        assertJanuaryThirdBeer(result[2])
    }

    @Test
    fun insertTwoDaysEntriesAndDeleteOneDay() {
        insertTwoDays()

        entryDao.deleteEntriesForDay(JAN_FIRST_FORMATTED_DATE)
        var result = entryDao.getEntriesForDay(createFormattedDateStringFor(0, 1, 2019))
        assertEquals(0, result.size)

        result = entryDao.getEntriesForDay(createFormattedDateStringFor(0, 3, 2019))
        assertEquals(3, result.size)
        assertJanuaryThirdWater(result[0])
        assertJanuaryThirdTea(result[1])
        assertJanuaryThirdBeer(result[2])
    }

    @Test
    fun queryForBeers() {
        insertTwoDays()
        val result = entryDao.getEntriesForDrinkType(beerId)
        assertEquals(2, result.size)
        assertJanuaryFirstBeer(result[0])
        assertJanuaryThirdBeer(result[1])
    }

    @Test
    fun updateEntryAmount() {
        entryDao.insertEntry(januaryFirstBeer)
        val afterInsert = entryDao.getEntriesForDay(JAN_FIRST_FORMATTED_DATE)[0]

        val updatedEntry = afterInsert.update(amount = 32)
        entryDao.insertEntry(updatedEntry)

        val actual = entryDao.getEntriesForDay(JAN_FIRST_FORMATTED_DATE)[0]
        assertEquals(32, actual.amount)
    }

    private fun insertTwoDays() {
        entryDao.apply {
            insertEntry(januaryFirstThree.createEntryWith(amount = 16, drinkTypeId =beerId ))
            insertEntry(januaryFirstTwo.createEntryWith(amount = 8, drinkTypeId = teaId))
            insertEntry(januaryFirstOne.createEntryWith(amount = 40, drinkTypeId = waterId))

            insertEntry(januaryThirdThree.createEntryWith(amount = 16, drinkTypeId = beerId))
            insertEntry(januaryThirdTwo.createEntryWith(amount = 8, drinkTypeId = teaId))
            insertEntry(januaryThirdOne.createEntryWith(amount = 40, drinkTypeId = waterId))
        }
    }

    private fun assertJanuaryFirstWater(actual: Entry) {
        actual.assert(
            januaryFirstWater.time,
            JAN_FIRST_FORMATTED_DATE,
            waterId,
            40
        )
    }

    private fun assertJanuaryFirstTea(actual: Entry) {
        actual.assert(
            januaryFirstTea.time,
            JAN_FIRST_FORMATTED_DATE,
            teaId,
            8
        )
    }

    private fun assertJanuaryFirstBeer(actual: Entry) {
        actual.assert(
            januaryFirstBeer.time,
            JAN_FIRST_FORMATTED_DATE,
            beerId,
            16
        )
    }

    private fun assertJanuaryThirdWater(actual: Entry) {
        actual.assert(
            januaryThirdWater.time,
            JAN_THIRD_FORMATTED_DATE,
            waterId,
            40
        )
    }

    private fun assertJanuaryThirdTea(actual: Entry) {
        actual.assert(
            januaryThirdTea.time,
            JAN_THIRD_FORMATTED_DATE,
            teaId,
            8
        )
    }

    private fun assertJanuaryThirdBeer(actual: Entry) {
        actual.assert(
            januaryThirdBeer.time,
            JAN_THIRD_FORMATTED_DATE,
            beerId,
            16
        )
    }


    private fun Entry.assert(
        expectedTime: Long,
        expectedDate: String,
        expectedDrinkTypeId: Int,
        expectedAmount: Int
    ) {
        this.apply {
            assertEquals(expectedTime, time)
            assertEquals(expectedDate, day)
            assertEquals(expectedDrinkTypeId, drinkTypeId)
            assertEquals(expectedAmount, amount)
        }
    }

    companion object {
        private const val WATER_ICON_RESOURCE = "WATER_ICON_RESOURCE"
        private const val TEA_ICON_RESOURCE = "TEA_ICON_RESOURCE"
        private const val BEER_ICON_RESOURCE = "BEER_ICON_RESOURCE"
        private const val JAN_FIRST_FORMATTED_DATE = "0/1/2019"
        private const val JAN_THIRD_FORMATTED_DATE = "0/3/2019"
    }
}