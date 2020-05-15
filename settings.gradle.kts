rootProject.name = "Porygen"

listOf("core", "render", "sponge-7").forEach {
  include(it)
  project(":$it").name = "porygen-$it"
}
