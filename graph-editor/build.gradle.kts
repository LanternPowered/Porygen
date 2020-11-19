import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  java
  idea
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("org.jetbrains.compose") version "0.2.0-build124"
}

ext.set("serialization", true)
ext.set("javaTarget", 13)

repositories {
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
  jvm()

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":porygen-core"))
        implementation(project(":porygen-graph"))
        implementation(compose.desktop.common)
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation(compose.desktop.all)
      }
    }
  }
}

compose.desktop {
  application {
    mainClass = "org.lanternpowered.porygen.EditorKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
    }
  }
}
