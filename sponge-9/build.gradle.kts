plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  // id("org.spongepowered.plugin") version "0.8.1"
}

ext.set("serialization", true)

repositories {
  maven("https://repo.spongepowered.org/maven")
}

kotlin {
  jvm()

  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(project(":porygen-core"))
        implementation(project(":porygen-graph"))
        implementation("org.spongepowered:spongeapi:9.0.0")
      }
    }
  }
}

/*
sponge {
  plugin.apply {
    id = rootProject.name.toLowerCase()
    meta.apply {
      setName(rootProject.name)
      setVersion(project.version)
      setDescription("A polygonal world generator. This is the official world generator for Lantern.")
      authors = listOf("Cybermaxke")
    }
  }
}
*/
