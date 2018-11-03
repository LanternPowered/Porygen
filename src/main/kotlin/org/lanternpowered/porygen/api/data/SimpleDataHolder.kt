package org.lanternpowered.porygen.api.data

import org.lanternpowered.porygen.api.util.uncheckedCast
import java.util.concurrent.ConcurrentHashMap

open class SimpleDataHolder : DataHolder {

    // Data values stored within this cell
    private val dataValues = ConcurrentHashMap<DataKey<*>, Any?>()

    override fun <T> get(key: DataKey<T>): T? = this.dataValues[key].uncheckedCast()

    override fun <T> set(key: DataKey<T>, value: T) {
        this.dataValues[key] = value as Any?
    }

    override fun <T> remove(key: DataKey<T>): T? {
        return this.dataValues.remove(key).uncheckedCast()
    }
}
