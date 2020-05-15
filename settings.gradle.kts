rootProject.name = "Porygen"

listOf("core", "render").forEach {
    include(it)
    project(":$it").name = "porygen-$it"
}
