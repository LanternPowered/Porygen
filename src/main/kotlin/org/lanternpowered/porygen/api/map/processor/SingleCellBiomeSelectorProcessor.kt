package org.lanternpowered.porygen.api.map.processor

import com.flowpowered.noise.module.Module
import org.lanternpowered.porygen.api.GeneratorContext
import org.lanternpowered.porygen.api.map.CellMapView
import org.lanternpowered.porygen.api.map.gen.biome.SingleCellBiomeGenerator
import org.spongepowered.api.world.biome.BiomeType
import java.util.*
import kotlin.collections.ArrayList

/**
 * @property terrainHeightModule The module that provides the terrain height,
 *                               everything under 0 is water and above is land
 *                               < 0.0 is water
 *                               >= 0.0 & < 1.0 is normal
 *                               >= 1.0 is hills
 * @property temperatureModule The module that provides the temperature,
 *                             < 0.2 is cold, < 1.0 is medium, >= 1.0 is warm
 */
class SingleCellBiomeSelectorProcessor(
        val terrainHeightModule: Module,
        val temperatureModule: Module,
        val oceanBiomeTypes: List<BiomeType>,
        val landBiomeTypes: List<BiomeType>,
        val hillBiomeTypes: List<BiomeType>
) : CellMapViewProcessor {

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

    private val landBiomesByTemperature: EnumMap<TemperatureCategory, MutableList<BiomeType>> = EnumMap(TemperatureCategory::class.java)
    private val oceanBiomesByTemperature: EnumMap<TemperatureCategory, MutableList<BiomeType>> = EnumMap(TemperatureCategory::class.java)
    private val hillBiomesByTemperature: EnumMap<TemperatureCategory, MutableList<BiomeType>> = EnumMap(TemperatureCategory::class.java)

    init {
        fun filterByTemperature(biomeTypes: List<BiomeType>, map: EnumMap<TemperatureCategory, MutableList<BiomeType>>) {
            for (biomeType in biomeTypes) {
                map.computeIfAbsent(TemperatureCategory.fromTemperature(biomeType.temperature)) { ArrayList() }.add(biomeType)
            }
        }

        filterByTemperature(this.oceanBiomeTypes, this.oceanBiomesByTemperature)
        filterByTemperature(this.landBiomeTypes, this.landBiomesByTemperature)
        filterByTemperature(this.hillBiomeTypes, this.hillBiomesByTemperature)
    }

    override fun process(context: GeneratorContext, view: CellMapView) {
        for (cell in view.cells) {
            val random = context.random
            random.setSeed(context.seed xor cell.id)

            val x = cell.centerPoint.x.toDouble()
            val z = cell.centerPoint.y.toDouble()

            val terrainHeight = this.terrainHeightModule.getValue(x, 1.0, z)
            val temperature = this.temperatureModule.getValue(x, 1.0, z)

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

            cell[SingleCellBiomeGenerator.BIOME_DATA_KEY] = biomes[random.nextInt(biomes.size)]
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
                temperature < 0.2 -> TemperatureCategory.COLD
                temperature < 1.0 -> TemperatureCategory.MEDIUM
                else -> TemperatureCategory.WARM
            }
        }
    }
}
