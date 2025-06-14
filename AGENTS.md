# AGENTS

## SpeechAgent
- **Rol**: Generar el servicio que use SpeechRecognizer para capturar direcciones por voz.
- **Prompt base**: "Eres SpeechAgent, crea la clase com.correos.delivery.core.SpeechRecognitionService con SpeechRecognizer, RecognizerIntent y callback onResults() que envíe resultados a MainActivity."
- **Archivos/Módulos**:
  - `core/src/main/java/com/correos/delivery/core/SpeechRecognitionService.java`
  - `app/src/main/java/com/example/routes/MainActivity.kt`

## RouteAgent
- **Rol**: Implementar el cliente de la Google Maps Route Optimization API.
- **Prompt base**: "Eres RouteAgent, genera la clase com.correos.delivery.api.RouteOptimizer con Retrofit/OkHttp que reciba una lista de direcciones (lat,lon) y devuelva el orden óptimo."
- **Archivos/Módulos**:
  - `api/src/main/java/com/correos/delivery/api/RouteOptimizer.java`

## ExportAgent
- **Rol**: Transformar una lista de Address en un archivo GPX y lanzar el Intent de navegación.
- **Prompt base**: "Eres ExportAgent, crea GpxExporter en core/export/GpxExporter.java que genere un archivo GPX válido y un método launchNavigation(uri) en MainActivity."
- **Archivos/Módulos**:
  - `core/src/main/java/com/correos/delivery/export/GpxExporter.java`
  - `app/src/main/java/com/example/routes/MainActivity.kt`
