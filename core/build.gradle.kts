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
  // mingwX64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(kotlin("reflect"))
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation("it.unimi.dsi:fastutil:8.2.2")
      }
    }

    val nativeCommonMain by creating {}
    val nativeCommonTest by creating {}
  }
}
