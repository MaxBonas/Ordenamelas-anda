#!/usr/bin/env sh
DIR="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_JAR="$DIR/gradle/wrapper/gradle-wrapper.jar"
BASE64_FILE="$DIR/gradle/wrapper/gradle-wrapper.jar.base64"
JAVA_CMD="java"

if [ ! -f "$WRAPPER_JAR" ] && [ -f "$BASE64_FILE" ]; then
  base64 -d "$BASE64_FILE" > "$WRAPPER_JAR"
fi

if [ -n "$JAVA_HOME" ]; then
  JAVA_CMD="$JAVA_HOME/bin/java"
fi

exec "$JAVA_CMD" -Dorg.gradle.appname=gradlew -cp "$WRAPPER_JAR" org.gradle.wrapper.GradleWrapperMain "$@"
