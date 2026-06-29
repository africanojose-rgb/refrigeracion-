package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CalculationRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationDao {
    @Query("SELECT * FROM calculation_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<CalculationRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: CalculationRecord)

    @Delete
    suspend fun deleteRecord(record: CalculationRecord)

    @Query("DELETE FROM calculation_records")
    suspend fun clearAll()
}
