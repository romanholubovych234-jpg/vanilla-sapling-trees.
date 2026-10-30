#!/bin/sh
# Needs Java 21 installed. Downloads Gradle 8.8 once, then builds the mod.
cd "$(dirname "$0")"
if [ ! -x .gradle-dist/gradle-8.8/bin/gradle ]; then
  mkdir -p .gradle-dist
  curl -L https://services.gradle.org/distributions/gradle-8.8-bin.zip -o .gradle-dist/g.zip
  unzip -q -o .gradle-dist/g.zip -d .gradle-dist
fi
.gradle-dist/gradle-8.8/bin/gradle build
echo "Done. The mod is in build/libs/ (vanilla-sapling-trees-1.21.1-1.0.0.jar)"
