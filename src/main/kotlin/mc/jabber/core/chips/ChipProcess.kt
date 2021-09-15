package mc.jabber.core.chips

import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import net.minecraft.util.Identifier

/**
 * An abstract process that represents a chip process
 */
abstract class ChipProcess {
    /**
     * If this process should be run to generate state (will cause [receive] to be called with an empty data an extra time at start of step)
     */
    open val isInput = false

    /**
     * The [Identifier] for serialization, should ideally be as small as possible
     */
    abstract val id: Identifier

    /**
     * A [DirBitmask] representing the sides the chip can receive on
     */
    abstract val receiveDirections: DirBitmask
    /**
     * A [DirBitmask] representing the sides the chip can send on
     */
    abstract val sendDirections: DirBitmask

    /**
     * The main function that makes everything tick
     *
     * [data] is provided raw as a minor optimisation for chips that do not store data,
     * this specific chip's data can be found with `chipData[pos]`, use [assertType] to quickly qualify the type of the data
     *
     * @param data The data received on this simulation step
     * @param pos The position of this chip on the board
     * @param chipData Any data that chips have declared it wants to be stored, produced initially by [makeInitialStateEntry]
     *
     * @return The data this chip outputs for this step
     *
     */
    @Suppress("KDocUnresolvedReference")
    abstract fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?
    ): CardinalData

    /**
     * Called *once per chip* in board setup, will not be called on deserialization
     *
     * @return The data to be stored by default in the state
     */
    open fun makeInitialStateEntry(): NbtTransformable<*>? {
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChipProcess

        if (isInput != other.isInput) return false
        if (id != other.id) return false
        if (receiveDirections != other.receiveDirections) return false
        if (sendDirections != other.sendDirections) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isInput.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + receiveDirections.hashCode()
        result = 31 * result + sendDirections.hashCode()
        return result
    }
}
