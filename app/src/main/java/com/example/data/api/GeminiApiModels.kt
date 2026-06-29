package com.example.data.api



data class GeminiPart(
    val text: String
)

data class GeminiContent(
    val parts: List<GeminiPart>
)

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

data class GeminiCandidate(
    val content: GeminiContent
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)
