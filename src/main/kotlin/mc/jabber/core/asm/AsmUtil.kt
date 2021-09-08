package mc.jabber.core.asm

import codes.som.anthony.koffee.MethodAssembly
import codes.som.anthony.koffee.insns.jvm.*
import mc.jabber.Global
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.math.Vec2I
import mc.jabber.minecraft.items.ChipItem
import net.minecraft.util.Identifier

/**
 * Makes a new vec2i and leaves it on the stack
 */
fun MethodAssembly.makeVec2I(vec2I: Vec2I) {
    new(Vec2I::class)
    dup
    ldc(vec2I.x)
    ldc(vec2I.y)
    invokespecial(
        Vec2I::class,
        "<init>",
        returnType = void,
        parameterTypes = arrayOf(int, int)
    )
}

/**
 * Takes the id from the chip process, and adds code to pull that chip process by the ID at runtime
 *
 * Process is left on the stack
 */
fun MethodAssembly.lookupChipProcess(process: ChipProcess) {
    getstatic(Global::class, "PROCESS_ITEM_MAP", HashMap::class)
    new(Identifier::class)
    dup
    ldc(process.id.toString())
    invokespecial(
        Identifier::class,
        "<init>",
        returnType = void,
        parameterTypes = arrayOf(String::class)
    )
    invokevirtual(HashMap::class, "get", "(Ljava/lang/Object;)Ljava/lang/Object;")
    checkcast(ChipItem::class)
    invokevirtual(ChipItem::class, "getProcess", "()Lmc/jabber/core/chips/ChipProcess;")
    checkcast(ChipProcess::class)
}
