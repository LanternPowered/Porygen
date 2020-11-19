plugins {
  kotlin("multiplatform") version "1.4.10" apply false
  kotlin("plugin.serialization") version "1.4.10" apply false
  id("net.minecrell.licenser") version "0.4.1"
}

allprojects {
  group = "org.lanternpowered"
  version = "1.0-SNAPSHOT"

  repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlinx")
  }
}

subprojects {
  // Execute after the individual gradle build files are evaluated
  afterEvaluate {
    apply(plugin = "net.minecrell.licenser")

    fun setupKotlinSettings(
        languageVersion: (name: String) -> Unit,
        enableLanguageFeature: (name: String) -> Unit,
        useExperimentalAnnotation: (name: String) -> Unit
    ) {
      languageVersion("1.4")

      enableLanguageFeature("InlineClasses")
      enableLanguageFeature("NewInference")
      enableLanguageFeature("NonParenthesizedAnnotationsOnFunctionalTypes")

      useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
      useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
      useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
      useExperimentalAnnotation("kotlin.experimental.ExperimentalTypeInference")
      useExperimentalAnnotation("kotlinx.serialization.UnstableDefault")
      useExperimentalAnnotation("kotlinx.serialization.ExperimentalSerializationApi")

      // Compose
      useExperimentalAnnotation("androidx.compose.foundation.layout.ExperimentalLayout")
    }

    val multiplatform = extensions.findByType<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>()
    val serialization = if (ext.has("serialization")) ext.get("serialization") as? Boolean ?: false else false

    fun serialization(module: String) =
        "org.jetbrains.kotlinx:kotlinx-serialization-$module:1.0.1"

    multiplatform?.apply {
      sourceSets {
        val commonMain by getting {
          dependencies {
            implementation(kotlin("stdlib-common"))
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

        findByName("jvmMain")?.apply {
          dependencies {
            implementation(kotlin("stdlib-jdk8"))
          }
        }

        findByName("jvmTest")?.apply {
          dependencies {
            implementation(kotlin("test-junit"))
          }
        }

        findByName("jsMain")?.apply {
          dependencies {
            implementation(kotlin("stdlib-js"))
          }
        }

        findByName("jsTest")?.apply {
          dependencies {
            implementation(kotlin("test-js"))
          }
        }

        val nativeCommonMain = findByName("nativeCommonMain")
        nativeCommonMain?.apply {
          dependsOn(commonMain)
        }
        val nativeCommonTest = findByName("nativeCommonTest")
        nativeCommonTest?.apply {
          dependsOn(commonTest)
        }

        fun applyNativeCommon(target: String) {
          findByName("${target}Main")?.apply {
            if (nativeCommonMain != null)
              dependsOn(nativeCommonMain)
          }
          findByName("${target}Test")?.apply {
            if (nativeCommonTest != null)
              dependsOn(nativeCommonTest)
          }
        }

        arrayOf(
            "androidNativeArm32", "androidNativeArm64", "iosArm32", "iosArm64", "iosX64",
            "linuxArm64", "linuxArm32Hfp", "linuxMips32", "linuxMipsel32", "linuxX64", "macosX64",
            "mingwX64", "mingwX86", "wasm32"
        ).forEach(::applyNativeCommon)

        all {
          languageSettings.apply {
            fun languageVersion(version: String) {
              apiVersion = version
              languageVersion = version
            }
            setupKotlinSettings(::languageVersion, ::enableLanguageFeature, ::useExperimentalAnnotation)
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

    val javaTarget: Int = (if (ext.has("javaTarget")) ext.get("javaTarget") as? Int else null) ?: 8

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().forEach {
      it.kotlinOptions.apply {
        jvmTarget = if (javaTarget > 8) javaTarget.toString() else "1.$javaTarget"

        val args = mutableListOf<String>()
        args += "-Xjvm-default=all"
        args += "-Xallow-result-return-type"
        args += "-Xemit-jvm-type-annotations"

        fun useExperimentalAnnotation(name: String) {
          args += "-Xuse-experimental=$name"
        }

        fun enableLanguageFeature(name: String) {
          args += "-XXLanguage:+$name"
        }

        fun languageVersion(version: String) {
          apiVersion = version
          languageVersion = version
        }

        setupKotlinSettings(::languageVersion, ::enableLanguageFeature, ::useExperimentalAnnotation)

        freeCompilerArgs = args
      }
    }

    license {
      header = rootProject.file("HEADER.txt")
      newLine = false
      ignoreFailures = false

      val sourceSetContainer = project.the<SourceSetContainer>()

      if (multiplatform != null) {
        val temp = mutableListOf<SourceSet>()
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

      ext {
        set("name", rootProject.name)
        set("url", "https://www.lanternpowered.org")
        set("organization", "LanternPowered")
      }
    }
  }
}
