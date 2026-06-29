package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.CalculationRecord
import com.example.data.repository.CalculationRepository
import com.example.data.service.GeminiService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState {
    object Idle : UiState
    object Loading : UiState
    data class Success(val record: CalculationRecord) : UiState
    data class Error(val message: String) : UiState
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = CalculationRepository(database.calculationDao())
    private val geminiService = GeminiService()

    // Active Screen Tab
    var currentTab by mutableStateOf(Tab.Dashboard)

    // Form inputs state
    var equipmentType by mutableStateOf("")
    var refrigerantType by mutableStateOf("")
    var ambientTemp by mutableStateOf("")
    var evaporatorTemp by mutableStateOf("")
    var condensatorTemp by mutableStateOf("")

    // Calculation result state
    var uiState: UiState by mutableStateOf(UiState.Idle)

    // History list Flow
    val historyState: StateFlow<List<CalculationRecord>> = repository.allRecords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Selected record for viewing details in Results tab
    var selectedRecord: CalculationRecord? by mutableStateOf(null)

    fun onTabSelected(tab: Tab) {
        currentTab = tab
    }

    fun selectRecord(record: CalculationRecord) {
        selectedRecord = record
        uiState = UiState.Success(record)
        currentTab = Tab.Results
    }

    fun clearForm() {
        equipmentType = ""
        refrigerantType = ""
        ambientTemp = ""
        evaporatorTemp = ""
        condensatorTemp = ""
        uiState = UiState.Idle
        selectedRecord = null
    }

    fun performCalculation() {
        val ampTemp = ambientTemp.toDoubleOrNull()
        val evapTemp = evaporatorTemp.toDoubleOrNull()
        val condTemp = condensatorTemp.toDoubleOrNull()

        if (equipmentType.isEmpty() || refrigerantType.isEmpty() || ampTemp == null || evapTemp == null || condTemp == null) {
            uiState = UiState.Error("Por favor, complete todos los campos técnicos con valores numéricos válidos.")
            currentTab = Tab.Results
            return
        }

        uiState = UiState.Loading
        currentTab = Tab.Results

        viewModelScope.launch {
            try {
                // Call Gemini for expert analysis and technical recommendation
                val recommendation = geminiService.calculateRefrigerantCharge(
                    equipmentType = equipmentType,
                    refrigerantType = refrigerantType,
                    ambientTemp = ampTemp,
                    evaporatorTemp = evapTemp,
                    condensatorTemp = condTemp
                )

                // Estimate charge in grams based on thermodynamic rules
                val base = when (equipmentType.lowercase()) {
                    "split" -> 750
                    "heladera" -> 160
                    "aire_central" -> 1500
                    "comercial" -> 900
                    else -> 450
                }
                val factorGas = when (refrigerantType.lowercase()) {
                    "r134a" -> 1.0
                    "r410a" -> 1.15
                    "r600" -> 0.38
                    "r22" -> 1.1
                    "r404a" -> 1.05
                    else -> 1.0
                }
                val thermalDelta = (condTemp - evapTemp).coerceAtLeast(5.0)
                val calculatedGrams = (base * factorGas + thermalDelta * 6.5 + ampTemp * 1.8).toInt().coerceIn(60, 6000)

                val newRecord = CalculationRecord(
                    equipmentType = equipmentType,
                    refrigerantType = refrigerantType,
                    ambientTemp = ampTemp,
                    evaporatorTemp = evapTemp,
                    condensatorTemp = condTemp,
                    estimatedChargeGrams = calculatedGrams,
                    geminiResponse = recommendation,
                    timestamp = System.currentTimeMillis()
                )

                // Save directly to Room local DB
                repository.insert(newRecord)

                selectedRecord = newRecord
                uiState = UiState.Success(newRecord)
            } catch (e: Exception) {
                uiState = UiState.Error("Error al realizar el cálculo: ${e.message}")
            }
        }
    }

    fun deleteRecord(record: CalculationRecord) {
        viewModelScope.launch {
            repository.delete(record)
            if (selectedRecord?.id == record.id) {
                selectedRecord = null
                uiState = UiState.Idle
            }
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearAll()
            selectedRecord = null
            uiState = UiState.Idle
        }
    }
}

enum class Tab {
    Dashboard, Calculator, Results, Settings
}
