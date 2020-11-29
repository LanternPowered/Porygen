/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.specs.noise

import org.lanternpowered.porygen.graph.node.spec.NodeSpec
import org.lanternpowered.porygen.noise.module.source.Perlin

object PerlinSpec : NodeSpec("noise/perlin", "Perlin") {

  val frequency = input(
    id = "frequency",
    default = Perlin.DefaultFrequency
  )

  val lacunarity = input(
    id = "lacunarity",
    default = Perlin.DefaultLacunarity
  )

  val quality = input(
    id = "quality",
    default = Perlin.DefaultQuality
  )

  val octaves = input(
    id = "octaves",
    default = Perlin.DefaultOctaves
  )

  val persistence = input(
    id = "persistence",
    default = Perlin.DefaultPersistence
  )

  val seed = input(
    id = "seed",
    default = Perlin.DefaultSeed
  )

  val output = output("out") { node ->
    Perlin(
        frequency = node[frequency],
        lacunarity = node[lacunarity],
        quality = node[quality],
        octaves = node[octaves],
        persistence = node[persistence],
        seed = node[seed]
    )
  }
}
