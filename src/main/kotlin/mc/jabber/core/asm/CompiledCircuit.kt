package mc.jabber.core.asm

/**
 * Basic interface for runtime compiled circuits
 */
interface CompiledCircuit {
    fun setup()
    fun simulate()
}
