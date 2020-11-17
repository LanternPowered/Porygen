plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

ext.set("serialization", true)

kotlin {
  jvm()

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":porygen-core"))
      }
    }
  }
}
