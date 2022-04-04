package mc.jabber.core.asm

import codes.som.anthony.koffee.ClassAssembly
import codes.som.anthony.koffee.MethodAssembly
import codes.som.anthony.koffee.insns.jvm.*
import mc.jabber.Global
import mc.jabber.core.chips.ChipParams
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.math.Vec2I
import mc.jabber.minecraft.items.ChipItem
import net.minecraft.util.Identifier
import org.objectweb.asm.tree.MethodNode
import java.lang.invoke.MethodHandles

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

fun MethodAssembly.makeChipParams(params: ChipParams) {
    new(ChipParams::class)
    dup
    aconst_null
    aconst_null
    invokespecial(
        ChipParams::class,
        "<init>",
        returnType = void,
        parameterTypes = arrayOf(ChipParams::class, Function1::class)
    )
    params.longParams.forEach { (name, value) ->
        dup
        ldc(name)
        ldc(value)
        invokevirtual(ChipParams::class, "registerLong", "(Ljava/lang/String;J)V")
    }
}

/**
 * Takes the id from the chip process, and adds code to pull that chip process by the ID at runtime, and configures it
 *
 * Process is left on the stack
 */
fun MethodAssembly.lookupChipProcess(process: ChipProcess, fieldName: String, self: ClassAssembly) {
    val method = self.makeChipProcessGetter(process, fieldName)
    ldc(self.constantDynamic(process.id.toUnderscoreSeparatedString(), ChipProcess::class, h_invokestatic(self.node, method)))
}

fun ClassAssembly.makeChipProcessGetter(process: ChipProcess, fieldName: String): MethodNode {
    return method(
        private + final + static,
        "get\$$fieldName",
        ChipProcess::class,
        parameterTypes = arrayOf(MethodHandles.Lookup::class, String::class, Class::class)
    ) {
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

        if (process.params != process.copy(null).params) {
            makeChipParams(process.params)
            invokevirtual(
                ChipProcess::class,
                "copy",
                "(Lmc/jabber/core/chips/ChipParams;)Lmc/jabber/core/chips/ChipProcess;"
            )
        }
        areturn
    }
}
