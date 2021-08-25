package mc.jabber.core.asm

import mc.jabber.util.byteArray
import org.objectweb.asm.tree.ClassNode
import java.lang.invoke.MethodHandles

object JabberClassLoader {
    private val lookup: MethodHandles.Lookup = MethodHandles.lookup()

    fun defineClass(node: ClassNode): Class<*> {
        return lookup.defineClass(node.byteArray())
    }
}
