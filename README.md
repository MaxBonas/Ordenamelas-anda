# Ordenamelas-anda

Esqueleto de aplicación Android para optimizar rutas de reparto por voz.

## Módulos
- `app/` interfaz de usuario y lógica Android.
- `core/` modelos y utilidades de generación de rutas.
- `api/` clientes para servicios externos (Google Maps, OpenAI).

Incluye script `setup.sh` para preparar el entorno Android.

## Uso
Ejecuta `./setup.sh` para instalar JDK 11 y descargar las herramientas de línea de comandos de Android. El script configura la variable `ANDROID_SDK_ROOT` y añade `sdkmanager` al `PATH` en tu `~/.bashrc`. Tras la instalación abre una nueva terminal o ejecuta `source ~/.bashrc` para que los cambios surtan efecto.

Para compilar la aplicación sin instalar Gradle manualmente utiliza el wrapper incluido.
El JAR del wrapper se almacena en formato base64 para evitar subir archivos binarios. El script `gradlew` lo decodifica automáticamente al ejecutarse.

```bash
./gradlew assemble
```

El script `setup.sh` ahora también instala `platform-tools`, `platforms;android-33` y `build-tools;33.0.0` para que el proyecto pueda compilarse sin pasos manuales.

Para ejecutar las pruebas unitarias:

```bash
./gradlew test
```

La clave de la Google Maps Route Optimization API debe proporcionarse en la variable de entorno `GOOGLE_MAPS_API_KEY` (o en la propiedad del sistema `google.maps.apiKey`) antes de ejecutar la aplicación.
