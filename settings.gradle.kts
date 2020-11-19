rootProject.name = "Porygen"

listOf("core", "graph", "graph-editor", "render", "sponge-7").forEach {
  include(it)
  project(":$it").name = "porygen-$it"
}

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }

  resolutionStrategy.eachPlugin {
    if (requested.id.id == "org.jetbrains.compose") {
      val version = requested.version ?: "0.1.0-build113"
      useModule("org.jetbrains.compose:compose-gradle-plugin:$version")
    }
  }
}