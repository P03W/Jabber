package data

import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.CircuitDataStorage
import mc.jabber.core.math.Vec2I
import org.junit.jupiter.api.Test

class CircuitDataStorageTest {
    @Test
    fun testPutAndRetrieve() {
        val data = CircuitDataStorage(3, 4)
        val point = Vec2I(2, 1)
        val dataToStore = CardinalData(null, null, null, null)
        data[point] = dataToStore

        assert(data[point] === dataToStore)
    }
}
