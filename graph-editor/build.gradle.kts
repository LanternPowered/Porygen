plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("org.jetbrains.compose") version "1.3.0-beta03"
}

ext.set("serialization", true)

repositories {
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
  // jvm()
  js {
    binaries.executable()
    browser {
      testTask {
        // TODO: Figure out why this runs te main js and causes an NPE
        enabled = false
//        testLogging.showStandardStreams = true
//        useKarma {
//          useChromeHeadless()
//        }
      }
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":porygen-core"))
        implementation(project(":porygen-graph"))
        implementation(compose.runtime)
        // implementation(compose.desktop.common)
      }
    }
    val jsMain by getting {
      dependencies {
        implementation(compose.web.core)
        implementation(compose.web.svg)
      }
    }
    val jsTest by getting {
      dependencies {
        implementation(compose.web.testUtils)
      }
    }
//    val jvmMain by getting {
//      dependencies {
//        implementation(compose.desktop.currentOs)
//      }
//    }
  }
}


//compose.desktop {
//  application {
//    mainClass = "org.lanternpowered.porygen.editor.MainKt"
//
//    nativeDistributions {
//      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//    }
//  }
//}
