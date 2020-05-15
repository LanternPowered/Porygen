plugins {
  kotlin("multiplatform")
}

kotlin {
  jvm()
  js()
  mingwX64()

  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation("it.unimi.dsi:fastutil:8.2.2")
      }
    }
    val nativeCommonMain by creating {}
    val nativeCommonTest by creating {}
  }
}
