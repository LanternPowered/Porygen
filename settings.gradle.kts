rootProject.name = "Porygen"

listOf("core", "graph", "graph-editor", "render", "sponge-9").forEach {
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
}
