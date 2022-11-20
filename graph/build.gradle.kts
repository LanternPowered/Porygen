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

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":porygen-core"))
        implementation(kotlin("reflect"))
      }
    }
    val jvmMain by getting {
      dependencies {
        // TODO: For some reason we need this dependency here as well, if
        //   we want to use the Int2ObjectMap from the core project
        implementation("it.unimi.dsi:fastutil:8.2.2")
      }
    }
  }
}
