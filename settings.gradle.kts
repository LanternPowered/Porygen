rootProject.name = "Porygen"

listOf("core", "graph", "graph-editor", "render", "sponge-7").forEach {
  include(it)
  project(":$it").name = "porygen-$it"
}
