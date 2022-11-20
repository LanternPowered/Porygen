# Porygen [![Build Status](https://travis-ci.org/LanternPowered/Porygen.svg?branch=master)](https://travis-ci.org/LanternPowered/Porygen) [![Discord](https://img.shields.io/badge/chat-on%20discord-6E85CF.svg)](https://discord.gg/ArSrsuU)

A polygonal world generator. This is the official world generator that will be used by the Lantern server, but is provided as a plugin to make it usable on any other sponge implementation.

* [Source]
* [Issues]
* [Wiki]

## Modules
* core
  * Core package with all the noise modules and world generators. 
  * Supports `java`, `js` and `native`.
* graph
  * Graph package that allows the modules to be connected using nodes with input and output ports. 
    Node structures can be serialized/deserialized and can be built using the node editor.
  * Supports `java` and `js`
* graph-editor
  * Graph editor web application.
* render
  * Temporary package with test examples that render to java frames.
* sponge-7
  * Sponge plugin that allows graphs to be used for Minecraft terrain generation.

## Prerequisites
* Java 17

## Clone
The following steps will ensure your project is cloned properly.

1. `git clone https://github.com/LanternPowered/Porygen.git`
2. `cd Porygen`

## Building
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for Windows systems in place of any 'gradle' command.

In order to build Porygen you simply need to run the `gradle build` command.

## IDE Setup
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for Windows systems in place of any 'gradle' command.

__For [IntelliJ]__
  1. Make sure you have the Gradle plugin enabled (File > Settings > Plugins)
  2. Click File > New > Project from Existing Sources > Gradle and select the root folder for Porygen
  3. Select Use customizable gradle wrapper if you do not have Gradle installed.

[Gradle]: https://www.gradle.org/
[Source]: https://github.com/LanternPowered/Porygen
[Issues]: https://github.com/LanternPowered/Porygen/issues
[Wiki]: https://github.com/LanternPowered/Porygen/wiki
[MIT License]: https://www.tldrlegal.com/license/mit-license
