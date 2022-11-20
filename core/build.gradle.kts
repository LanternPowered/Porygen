plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

ext.set("serialization", true)

kotlin {
  jvm()
  js {
    browser()
    nodejs()
  }
  mingwX64()

  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation("it.unimi.dsi:fastutil-core:8.5.9")
      }
    }

    val jsAndNativeMain by creating {}
    val jsAndNativeTest by creating {}

    val nativeMain by creating {}
    val nativeTest by creating {}
  }
}
