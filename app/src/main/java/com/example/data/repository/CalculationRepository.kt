package com.example.data.repository

import com.example.data.dao.CalculationDao
import com.example.data.model.CalculationRecord
import kotlinx.coroutines.flow.Flow

class CalculationRepository(private val calculationDao: CalculationDao) {
    val allRecords: Flow<List<CalculationRecord>> = calculationDao.getAllRecords()

    suspend fun insert(record: CalculationRecord) {
        calculationDao.insertRecord(record)
    }

    suspend fun delete(record: CalculationRecord) {
        calculationDao.deleteRecord(record)
    }

    suspend fun clearAll() {
        calculationDao.clearAll()
    }
}
