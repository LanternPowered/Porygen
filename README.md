# Porygen [![Build Status](https://travis-ci.org/LanternPowered/Porygen.svg?branch=master)](https://travis-ci.org/LanternPowered/Porygen) [![Discord](https://img.shields.io/badge/chat-on%20discord-6E85CF.svg)](https://discord.gg/ArSrsuU)

A polygonal world generator. This is the official world generator that will be used by the Lantern server, but is provided as a plugin to make it usable on any other sponge implementation.

* [Source]
* [Issues]
* [Wiki]

## Prerequisites
* [Java 8]

## Clone
The following steps will ensure your project is cloned properly.

1. `git clone https://github.com/LanternPowered/Porygen.git`
2. `cd Porygen`

## Building
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for Windows systems in place of any 'gradle' command.

In order to build Porygen you simply need to run the `gradle build` command. You can find the compiled JAR file in `./build/libs` labeled similarly to 'porygen-x.x.x-SNAPSHOT.jar'.

## IDE Setup
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for Windows systems in place of any 'gradle' command.

__For [Eclipse]__
  1. Run `gradle eclipse`
  2. Import Porygen as an existing project (File > Import > General)
  3. Select the root folder for Porygen
  4. Check Porygen when it finishes building and click **Finish**

__For [IntelliJ]__
  1. Make sure you have the Gradle plugin enabled (File > Settings > Plugins)
  2. Click File > New > Project from Existing Sources > Gradle and select the root folder for Porygen
  3. Select Use customizable gradle wrapper if you do not have Gradle installed.

[Eclipse]: https://eclipse.org/
[Gradle]: https://www.gradle.org/
[IntelliJ]: http://www.jetbrains.com/idea/
[Source]: https://github.com/LanternPowered/Porygen
[Java 8]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[Issues]: https://github.com/LanternPowered/Porygen/issues
[Wiki]: https://github.com/LanternPowered/Porygen/wiki
[MIT License]: https://www.tldrlegal.com/license/mit-license