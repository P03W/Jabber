package mc.jabber.core.chips

import kotlinx.serialization.Serializable
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

@Serializable
abstract class ChipProcess {
    /**
     * If this process should be run to generate state (will cause [receive] to be called with an empty data an extra time at start of step)
     */
    open val isInput = false

    /**
     * The main function that makes everything tick
     *
     * [data] is provided raw as a minor optimisation for chips that do not store data,
     * this specific chip's data can be found with `chipData[pos]`, use [assertType] to quickly qualify the data
     *
     * @param data The data received on this simulation step
     * @param pos The position of this chip on the board
     * @param chipData Any data the chip has declared it wants stored in [makeInitialStateEntry]
     *
     * @return The data this chip outputs for this step
     *
     */
    @Suppress("KDocUnresolvedReference")
    abstract fun <T : NbtTransformable<*>> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData<T>

    /**
     * Called *once per chip* in board setup, will not be called on deserialization
     *
     * @return The data to be stored by default in the state
     */
    open fun makeInitialStateEntry(): NbtTransformable<*>? {
        return null
    }
}
