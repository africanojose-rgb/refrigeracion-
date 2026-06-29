package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_records")
data class CalculationRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val equipmentType: String,
    val refrigerantType: String,
    val ambientTemp: Double,
    val evaporatorTemp: Double,
    val condensatorTemp: Double,
    val estimatedChargeGrams: Int,
    val geminiResponse: String,
    val timestamp: Long = System.currentTimeMillis()
)
