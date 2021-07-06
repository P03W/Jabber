package data

import mc.jabber.data.util.DualHashMap
import org.junit.jupiter.api.Test

class DualHashmapTest {
    val key = 12354
    val valueA = -35478
    val valueB = "This is an example string"

    @Test
    fun testDualHashmapEmptiness() {
        assert(DualHashMap<Int, Int, Int>().isEmpty())
    }

    @Test
    fun testDualHashmapSetAndLookup() {
        val map = DualHashMap<Int, Int, String>()

        map.set(key, valueA, valueB)

        val got = map[key]
        assert(got.first == valueA)
        assert(got.second == valueB)
    }

    @Test
    fun testDualHashmapEquality() {
        val mapA = DualHashMap<Int, Int, String>()
        val mapB = DualHashMap<Int, Int, String>()
        val mapC = DualHashMap<Int, String, Short>()

        mapA.set(key, valueA, valueB)
        mapB.set(key, valueA, valueB)
        mapC.set(key, valueB, 42)

        assert(mapA == mapB)
        assert(mapA != mapC)
    }

    @Test
    fun testDualHashMapClearing() {
        val map = DualHashMap<Int, Int, String>()

        map.set(key, valueA, valueB)

        map.clear()

        assert(map.isEmpty())
    }

    @Test
    fun testDualHashmapRemoval() {
        val map = DualHashMap<Int, Int, String>()

        map.set(key, valueA, valueB)

        map.remove(key)

        assert(map.isEmpty())
    }
}
