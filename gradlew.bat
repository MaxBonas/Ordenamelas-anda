@echo off
set DIR=%~dp0
set JAVA_CMD=java
if not "%JAVA_HOME%"=="" set JAVA_CMD=%JAVA_HOME%\bin\java
"%JAVA_CMD%" -Dorg.gradle.appname=gradlew -cp "%DIR%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
