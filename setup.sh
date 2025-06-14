#!/bin/bash
# Script to install JDK 11 and Android SDK command line tools
set -e

sudo apt-get update
sudo apt-get install -y openjdk-11-jdk wget unzip

# Set up Android SDK directory
export ANDROID_SDK_ROOT="$HOME/android-sdk"
mkdir -p "$ANDROID_SDK_ROOT/cmdline-tools"

# Download and extract command line tools
cd /tmp
SDK_TOOLS_ZIP=commandlinetools-linux.zip
wget -q https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip -O "$SDK_TOOLS_ZIP"
unzip -q "$SDK_TOOLS_ZIP"
rm "$SDK_TOOLS_ZIP"
mkdir -p "$ANDROID_SDK_ROOT/cmdline-tools/latest"
mv cmdline-tools/* "$ANDROID_SDK_ROOT/cmdline-tools/latest/"

# Configure environment variables
if ! grep -q ANDROID_SDK_ROOT "$HOME/.bashrc"; then
  echo "export ANDROID_SDK_ROOT=$ANDROID_SDK_ROOT" >> "$HOME/.bashrc"
  echo "export PATH=\$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools" >> "$HOME/.bashrc"
fi
export PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools

# Accept licenses and install required SDK packages
yes | sdkmanager --licenses
sdkmanager --install "platform-tools" "platforms;android-33" "build-tools;33.0.0"

