/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.map.processor

import org.lanternpowered.porygen.map.CellMapView
import org.lanternpowered.porygen.value.Value2

/**
 * @property terrainHeightModule The module that provides the terrain height,
 *                               everything under 0 is water and above is land
 *                               < 0.0 is water
 *                               >= 0.0 & < 1.0 is normal
 *                               >= 1.0 is hills
 * @property temperatureModule The module that provides the temperature,
 *                             < 0.2 is cold, < 1.0 is medium, >= 1.0 is warm
 */
class SingleCellBiomeSelectorProcessor<Biome>(
    val terrainHeightModule: Value2,
    val temperatureModule: Value2,
    val oceanBiomeTypes: List<Biome>,
    val landBiomeTypes: List<Biome>,
    val hillBiomeTypes: List<Biome>
) : CellMapProcessor {

  /*
  // TODO: Height modifiers aren't properly implemented in sponge... And they should be base height and variation, not min/max.

  private val biomeCache: LoadingCache<HeightScaledBiome, BiomeType>? = Caffeine.newBuilder()
          //.uncheckedCast<Caffeine<HeightScaledBiome, BiomeType>>()
          .build { scaledBiome ->
              val biome = scaledBiome.biomeType
              VirtualBiomeType.builder()
                      .temperature(biome.temperature)
                      .humidity(biome.humidity)
                      .name(biome.name)
                      .persistedType((biome as? VirtualBiomeType)?.persistedType ?: biome)
                      .settingsBuilder { world ->
                          val settings = biome.createDefaultGenerationSettings(world)
                          settings.minHeight
                          settings
                      }
                      .build("${biome.id.replace("minecraft", "porygen")}_height_${scaledBiome.height}")
          }

  private data class HeightScaledBiome(
          val biomeType: BiomeType,
          val height: Double
  )

  */

  private val landBiomesByTemperature: Map<TemperatureCategory, MutableList<Biome>> = mapOf()
  private val oceanBiomesByTemperature: Map<TemperatureCategory, MutableList<Biome>> = mapOf()
  private val hillBiomesByTemperature: Map<TemperatureCategory, MutableList<Biome>> = mapOf()

  init {
    fun filterByTemperature(biomeTypes: List<Biome>, map: Map<TemperatureCategory, MutableList<Biome>>) {
      for (biomeType in biomeTypes) {
        //map.computeIfAbsent(TemperatureCategory.fromTemperature(biomeType.temperature)) { ArrayList() }.add(biomeType)
      }
    }

    filterByTemperature(this.oceanBiomeTypes, this.oceanBiomesByTemperature)
    filterByTemperature(this.landBiomeTypes, this.landBiomesByTemperature)
    filterByTemperature(this.hillBiomeTypes, this.hillBiomesByTemperature)
  }

  override fun process(view: CellMapView) {
    for (cell in view.cells) {
      // val cellRandom = XorWowRandom(context.seed xor cell.id)

      val x = cell.centerPoint.x.toDouble()
      val y = cell.centerPoint.y.toDouble()

      val terrainHeight = this.terrainHeightModule[x, y]
      val temperature = this.temperatureModule[x, y]

      val biomesByTemperature = when {
        terrainHeight < 0 -> this.oceanBiomesByTemperature
        terrainHeight < 1.0 || this.hillBiomesByTemperature.isEmpty() -> this.landBiomesByTemperature
        else -> this.hillBiomesByTemperature
      }

      val temperatureCategory = TemperatureCategory.fromTemperature(temperature)
      val biomes = biomesByTemperature[temperatureCategory]
      // Fall back if no biomes in the biome category were found
          ?: biomesByTemperature[TemperatureCategory.MEDIUM]
          ?: biomesByTemperature[TemperatureCategory.WARM]
          ?: biomesByTemperature[TemperatureCategory.COLD]
          ?: throw IllegalStateException("No biomes found.")

      //cell[SingleCellBiomeGenerator.BIOME_DATA_KEY] = biomes[cellRandom.nextInt(biomes.size)]
    }
  }

}

/**
 * The temperature categories.
 */
enum class TemperatureCategory {
  COLD,
  MEDIUM,
  WARM,
  ;

  companion object {

    fun fromTemperature(temperature: Double): TemperatureCategory {
      return when {
        temperature < 0.2 -> COLD
        temperature < 1.0 -> MEDIUM
        else -> WARM
      }
    }
  }
}
