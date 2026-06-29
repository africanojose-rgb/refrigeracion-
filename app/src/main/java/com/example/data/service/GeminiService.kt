package com.example.data.service

import android.util.Log
import com.example.BuildConfig
import com.example.data.api.GeminiApiClient
import com.example.data.api.GeminiContent
import com.example.data.api.GeminiPart
import com.example.data.api.GeminiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiService {

    private val systemInstruction = """
        Actúa como un Ingeniero de Refrigeración Senior y Asesor Técnico Experto de AFRICANO Workshop.
        Tu tarea es analizar los parámetros técnicos de un equipo de refrigeración para determinar si el funcionamiento es óptimo, explicar las relaciones de presión-temperatura para el gas refrigerante seleccionado y dar recomendaciones de instalación o reparación muy profesionales y precisas.
        
        Recibirás los siguientes parámetros:
        - Tipo de Equipo
        - Tipo de Gas Refrigerante
        - Temperatura Ambiente (°C)
        - Temperatura de Evaporador (°C)
        - Temperatura de Condensador (°C)
        
        Por favor, responde en un formato claro y estructurado con:
        1. DIAGNÓSTICO TÉCNICO: Explica si el diferencial de temperaturas (salto térmico) es el adecuado para este tipo de gas y equipo.
        2. EXPLICACIÓN FÍSICA: Detalla los puntos de de saturación aproximados (relación presión-temperatura).
        3. RECOMENDACIONES DEL TÉCNICO: Proporciona 2 o 3 recomendaciones directas, breves e industriales (por ejemplo, verificar fugas, realizar vacío por X minutos, revisar válvula de expansión o limpieza de condensador).
        
        Mantén un tono profesional, técnico, directo y de confianza industrial. Responde en español.
    """.trimIndent()

    suspend fun calculateRefrigerantCharge(
        equipmentType: String,
        refrigerantType: String,
        ambientTemp: Double,
        evaporatorTemp: Double,
        condensatorTemp: Double
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.e("GeminiService", "API Key is missing or default placeholder")
            return@withContext "Error: API Key de Gemini no configurada. Configure su GEMINI_API_KEY en el panel de secretos de AI Studio."
        }

        val prompt = """
            Analiza los siguientes parámetros para cálculo de carga:
            - Tipo de Equipo: $equipmentType
            - Gas Refrigerante: $refrigerantType
            - Temperatura Ambiente: $ambientTemp °C
            - Temperatura de Evaporador: $evaporatorTemp °C
            - Temperatura de Condensador: $condensatorTemp °C
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(parts = listOf(GeminiPart(text = prompt)))
            ),
            systemInstruction = GeminiContent(
                parts = listOf(GeminiPart(text = systemInstruction))
            )
        )

        try {
            val response = GeminiApiClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "No se pudo obtener una respuesta del modelo de IA."
        } catch (e: Exception) {
            Log.e("GeminiService", "Error calling Gemini API: ${e.message}", e)
            "Error al conectar con la IA de Gemini: ${e.localizedMessage ?: e.message}. Por favor, verifique su conexión e intente nuevamente."
        }
    }
}
