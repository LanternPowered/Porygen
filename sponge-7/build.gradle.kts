plugins {
  kotlin("multiplatform")
}

repositories {
  maven("http://repo.spongepowered.org/maven")
}

kotlin.sourceSets {
  val jvmMain by getting {
    dependencies {
      implementation(project(":porygen-core"))
      implementation("org.spongepowered:spongeapi:7.1.0-SNAPSHOT")
    }
  }
}
