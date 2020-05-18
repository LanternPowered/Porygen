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
    val commonMain by getting {
      dependencies {
        implementation(project(":porygen-core"))
      }
    }

    val nativeCommonMain by creating {}
    val nativeCommonTest by creating {}
  }
}
