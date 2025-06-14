# Ordenamelas-anda

Esqueleto de aplicación Android para optimizar rutas de reparto por voz.

## Módulos
- `app/` interfaz de usuario y lógica Android.
- `core/` modelos y utilidades de generación de rutas.
- `api/` clientes para servicios externos (Google Maps, OpenAI).

Incluye script `setup.sh` para preparar el entorno Android.

## Uso
Ejecuta `./setup.sh` para instalar JDK 11 y descargar las herramientas de línea de comandos de Android. El script configura la variable `ANDROID_SDK_ROOT` y añade `sdkmanager` al `PATH` en tu `~/.bashrc`. Tras la instalación abre una nueva terminal o ejecuta `source ~/.bashrc` para que los cambios surtan efecto.

