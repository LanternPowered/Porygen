plugins {
  kotlin("multiplatform") version "1.3.72"
  id("net.minecrell.licenser") version "0.4.1"
}

allprojects {
  group = "org.lanternpowered"
  version = "1.0-SNAPSHOT"

  repositories {
    mavenCentral()
  }
}

subprojects {
  apply(plugin = "org.jetbrains.kotlin.multiplatform")
  apply(plugin = "net.minecrell.licenser")

  // Must be applied, otherwise the npm resolver will break the build
  // https://youtrack.jetbrains.com/issue/KT-34389
  // project.plugins.apply(org.jetbrains.kotlin.gradle.targets.js.npm.NpmResolverPlugin::class.java)

  kotlin {
    jvm()
    js()

    // val coroutinesVersion = "1.3.6"

    sourceSets {
      val commonMain by getting {
        dependencies {
          implementation(kotlin("stdlib-common"))
          // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutinesVersion")
        }
      }

      val commonTest by getting {
        dependencies {
          implementation(kotlin("test-common"))
          implementation(kotlin("test-annotations-common"))
        }
      }

      val jvmMain by getting {
        dependencies {
          implementation(kotlin("stdlib"))
          implementation("it.unimi.dsi:fastutil:8.2.2")
          // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
        }
      }

      val jvmTest by getting {
        dependencies {
          implementation(kotlin("test-junit"))
        }
      }

      val jsMain by getting {
        dependencies {
          implementation(kotlin("stdlib-js"))
          // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutinesVersion")
        }
      }

      val jsTest by getting {
        dependencies {
          implementation(kotlin("test-js"))
        }
      }

      all {
        languageSettings.apply {
          languageVersion = "1.3"
          apiVersion = "1.3"
          progressiveMode = true

          enableLanguageFeature("InlineClasses")
          enableLanguageFeature("NewInference")
          enableLanguageFeature("NonParenthesizedAnnotationsOnFunctionalTypes")

          useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
          useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
          useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
          useExperimentalAnnotation("kotlin.experimental.ExperimentalTypeInference")
        }
      }
    }
  }

  license {
    header = rootProject.file("HEADER.txt")
    newLine = false
    ignoreFailures = false

    // Map the kotlin source sets to normal source sets
    // so that they work in the license plugin.
    sourceSets {
      for ((name, kotlinSourceSet) in kotlin.sourceSets.asMap) {
        create(project.name + "_" + name) {
          allSource.source(kotlinSourceSet.kotlin)
        }
      }
    }

    include("**/*.kt")

    ext {
      set("name", rootProject.name)
      set("url", "https://www.lanternpowered.org")
      set("organization", "LanternPowered")
    }
  }
}
