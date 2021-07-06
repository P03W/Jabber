package mc.jabber.chips.abstract

import mc.jabber.data.CardinalData
import mc.jabber.data.serial.NbtTransformable
import mc.jabber.math.Vec2I

abstract class ChipProcess {
    abstract fun <T : NbtTransformable> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        state: HashMap<Vec2I, Any>
    ): CardinalData<T>

    /**
     * Called once in chip setup
     *
     * @return The data to be stored by default in the state
     */
    open fun makeInitialStateEntry(): NbtTransformable? {
        return null
    }
}
