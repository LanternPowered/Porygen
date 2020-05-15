plugins {
  kotlin("multiplatform")
}

kotlin {
  jvm()
  js()

  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation("it.unimi.dsi:fastutil:8.2.2")
      }
    }
  }
}
