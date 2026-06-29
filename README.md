# AFRICANO Workshop

Aplicación Android para **cálculo de carga de gas refrigerante** en equipos HVAC/R.
Usa **Gemini API** para diagnóstico técnico experto y recomendaciones en tiempo real.

## Características

- **Cálculo termodinámico** — Estima gramos de refrigerante según tipo de equipo, gas y temperaturas
- **IA Gemini** — Diagnóstico técnico, análisis presión-temperatura y recomendaciones industriales
- **Historial local** — Almacenamiento en Room DB con consultas y eliminación
- **Interfaz Material 3** — UI moderna con 4 pestañas: Dashboard, Calculadora, Resultados, Config

## Requisitos

- Android 7.0+ (API 24)
- [Gemini API Key](https://aistudio.google.com/)

## Configuración

1. Clona el repositorio
2. Abre en **Android Studio**
3. Crea un archivo `.env` en la raíz con tu API key:
   ```
   GEMINI_API_KEY=tu_api_key_aqui
   ```
4. Compila y ejecuta

## Tecnologías

| Componente | Tecnología |
|------------|------------|
| Lenguaje | Kotlin 2.2 |
| UI | Jetpack Compose + Material 3 |
| IA | Gemini API 2.0 Flash |
| Persistencia | Room (SQLite) |
| Networking | Retrofit + OkHttp + Moshi |
| Build | Gradle 9.6 + AGP 9.1 |
