import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  java
  idea
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("org.jetbrains.compose") version "0.1.0-build113"
}

ext.set("serialization", true)

repositories {
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
  implementation(project(":porygen-core"))
  implementation(compose.desktop.all)
}

compose.desktop {
  application {
    mainClass = "org.lanternpowered.porygen.EditorKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
    }
  }
}
