package mc.jabber.core.asm

/**
 * Basic interface for runtime compiled circuits
 */
interface CompiledCircuit {
    fun getVersion(): Int
    fun setup()
    fun simulate()
}
