plugins {
  kotlin("multiplatform")
}

ext.set("serialization", true)

kotlin {
  js {
    browser {}
  }
  repositories {
    maven("https://dl.bintray.com/kotlin/kotlinx")
    maven("https://kotlin.bintray.com/js-externals")
    maven("https://kotlin.bintray.com/kotlin-js-wrappers")
  }
  sourceSets {
    val jsMain by getting {
      dependencies {
        implementation(project(":porygen-graph"))
        implementation(project(":porygen-core"))

        val reactVersion = "16.13.1"
        val kotlinReactVersion = "$reactVersion-pre.105-kotlin-1.3.72"

        implementation("org.jetbrains:kotlin-react:$kotlinReactVersion")
        implementation("org.jetbrains:kotlin-react-dom:$kotlinReactVersion")
        implementation(npm("react", reactVersion))
        implementation(npm("react-dom", reactVersion))

        implementation(npm("litegraph.js", "0.7.8"))
      }
    }
  }
}
