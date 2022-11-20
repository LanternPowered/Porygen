plugins {
  kotlin("multiplatform") version "1.7.20" apply false
  kotlin("plugin.serialization") version "1.7.20" apply false
  id("org.cadixdev.licenser") version "0.6.1"
}

allprojects {
  group = "org.lanternpowered"
  version = "1.0.0"

  repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlinx")
  }
}

subprojects {
  // Execute after the individual gradle build files are evaluated
  afterEvaluate {
    apply(plugin = "org.cadixdev.licenser")

    val multiplatform = extensions.findByType<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>()
    val serialization = if (ext.has("serialization")) ext.get("serialization") as? Boolean ?: false else false

    fun serialization(module: String) =
        "org.jetbrains.kotlinx:kotlinx-serialization-$module:1.4.1"

    multiplatform?.apply {
      sourceSets {
        val commonMain by getting {
          dependencies {
            if (serialization) {
              implementation(serialization("core"))
              implementation(serialization("json"))
            }
          }
        }
        val commonTest by getting {
          dependencies {
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
          }
        }

        val jvmMain = findByName("jvmMain")
        val jvmTest = findByName("jvmTest")

        val jsAndNativeMain = findByName("jsAndNativeMain")
        val jsAndNativeTest = findByName("jsAndNativeTest")

        val jsAndJvmMain = findByName("jsAndJvmMain")
        val jsAndJvmTest = findByName("jsAndJvmTest")

        val jsMain = findByName("jsMain")
        val jsTest = findByName("jsTest")

        val nativeMain = findByName("nativeMain")
        val nativeTest = findByName("nativeTest")

        jvmTest?.dependencies {
          implementation(kotlin("test-junit"))
        }
        jsTest?.dependencies {
          implementation(kotlin("test-js"))
        }

        if (jsAndJvmMain != null) {
          jsAndJvmMain.dependsOn(commonMain)
          jsMain?.dependsOn(jsAndJvmMain)
          jvmMain?.dependsOn(jsAndJvmMain)
        }
        if (jsAndJvmTest != null) {
          jsAndJvmTest.dependsOn(commonTest)
          if (jsAndJvmMain != null)
            jsAndJvmTest.dependsOn(jsAndJvmMain)
          jvmTest?.dependsOn(jsAndJvmTest)
          jsTest?.dependsOn(jsAndJvmTest)
        }

        if (jsAndNativeMain != null) {
          jsAndNativeMain.dependsOn(commonMain)
          jsMain?.dependsOn(jsAndNativeMain)
          nativeMain?.dependsOn(jsAndNativeMain)
        }
        if (jsAndNativeTest != null) {
          jsAndNativeTest.dependsOn(commonTest)
          if (jsAndNativeMain != null)
            jsAndNativeTest.dependsOn(jsAndNativeMain)
          nativeTest?.dependsOn(jsAndNativeTest)
          jsTest?.dependsOn(jsAndNativeTest)
        }

        if (jvmMain != null)
          jvmTest?.dependsOn(jvmMain)
        if (jsMain != null)
          jsTest?.dependsOn(jsMain)
        if (nativeMain != null)
          nativeTest?.dependsOn(nativeMain)

        targets
          .filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>()
          .map { it.name }
          .forEach { target ->
            if (target == "native")
              return@forEach
            findByName("${target}Main")?.apply {
              if (nativeMain != null)
                dependsOn(nativeMain)
            }
            findByName("${target}Test")?.apply {
              if (nativeTest != null)
                dependsOn(nativeTest)
            }
          }

        targets
          .filterIsInstance<org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget>()
          .forEach { target ->
            target.compilations.forEach { compilation ->
              compilation.kotlinOptions.jvmTarget = 17.toString()
            }
          }

        all {
          languageSettings.apply {
            languageVersion = "1.8"

            enableLanguageFeature("InlineClasses")
            enableLanguageFeature("NewInference")
            enableLanguageFeature("NonParenthesizedAnnotationsOnFunctionalTypes")

            optIn("kotlin.ExperimentalUnsignedTypes")
            optIn("kotlin.contracts.ExperimentalContracts")
            optIn("kotlin.ExperimentalStdlibApi")
            optIn("kotlin.experimental.ExperimentalTypeInference")
            optIn("kotlin.time.ExperimentalTime")

            optIn("kotlinx.serialization.ExperimentalSerializationApi")

            // Compose
            optIn("org.jetbrains.compose.web.ExperimentalComposeWebApi")
            optIn("org.jetbrains.compose.web.ExperimentalComposeWebSvgApi")
            optIn("kotlinx.coroutines.DelicateCoroutinesApi")

            // Compose
            //optIn("androidx.compose.foundation.layout.ExperimentalLayout")
            //optIn("androidx.compose.foundation.ExperimentalFoundationApi")
          }
        }
      }
    }

    if (multiplatform == null) {
      dependencies {
        add("implementation", kotlin("stdlib-jdk8"))
        if (serialization) {
          add("implementation", serialization("core-jvm"))
        }
      }
    }

    license {
      header(rootProject.file("HEADER.txt"))
      newLine(false)
      ignoreFailures(false)

      val sourceSetContainer = project.the<SourceSetContainer>()

      if (multiplatform != null) {
        val temp = mutableSetOf<SourceSet>()
        for ((name, kotlinSourceSet) in multiplatform.sourceSets.asMap) {
          temp += sourceSetContainer.create(project.name + "_" + name) {
            allSource.source(kotlinSourceSet.kotlin)
          }
        }
        gradle.taskGraph.whenReady {
          // Remove them when the license plugin has detected them
          // so the dev environment doesn't get polluted
          sourceSetContainer.removeAll(temp)
        }
      }

      include("**/*.kt")

      properties {
        set("name", rootProject.name)
        set("url", "https://www.lanternpowered.org")
        set("organization", "LanternPowered")
      }
    }
  }
}
