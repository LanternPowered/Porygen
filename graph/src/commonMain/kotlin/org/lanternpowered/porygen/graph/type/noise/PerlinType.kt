/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.graph.type.noise

import org.lanternpowered.porygen.graph.NodeType
import org.lanternpowered.porygen.noise.module.source.Perlin

object PerlinType : NodeType("noise/perlin", "Perlin") {

  val frequency = input("frequency", Perlin.DEFAULT_FREQUENCY)
  val lacunarity = input("lacunarity", Perlin.DEFAULT_LACUNARITY)
  val quality = input("quality", Perlin.DEFAULT_QUALITY)
  val octaves = input("octaves", Perlin.DEFAULT_OCTAVES)
  val persistence = input("persistence", Perlin.DEFAULT_PERSISTENCE)
  val seed = input("seed", Perlin.DEFAULT_SEED)

  val output = output("out") { node ->
    Perlin(
        frequency = node[frequency].get(),
        lacunarity = node[lacunarity].get(),
        quality = node[quality],
        octaves = node[octaves].get(),
        persistence = node[persistence].get(),
        seed = node[seed].get()
    )
  }
}
