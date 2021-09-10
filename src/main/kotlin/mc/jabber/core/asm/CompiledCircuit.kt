package mc.jabber.core.asm

/**
 * Basic interface for runtime compiled circuits
 */
interface CompiledCircuit {
    // Simulation / management
    fun setup()
    fun simulate()

    // Serializtion
    fun getChipStorage(): HashMap<Vec2I, NbtTransformable>
    fun getChipState(): CircuitDataStorage

    // De-serialization
    fun constructFrom(chipStorage: HashMap<Vec2I, NbtTransformable>, chipState: CircuitDataStorage)
}
