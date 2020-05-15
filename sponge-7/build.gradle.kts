plugins {
  java
  idea
  kotlin("jvm")
  id("org.spongepowered.plugin") version "0.8.1"
}

repositories {
  maven("http://repo.spongepowered.org/maven")
}

dependencies {
  // implementation(kotlin("stdlib-jdk8"))
  implementation(project(":porygen-core"))
  implementation("org.spongepowered:spongeapi:7.1.0-SNAPSHOT")
}

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
