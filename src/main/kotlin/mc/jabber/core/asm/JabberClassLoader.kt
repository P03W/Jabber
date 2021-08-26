package mc.jabber.core.asm

import mc.jabber.core.asm.runtime.LookupTarget
import mc.jabber.util.byteArray
import org.objectweb.asm.tree.ClassNode
import java.lang.invoke.MethodHandles

/**
 * "Loads" a class node by using method handles to define a class in the `mc.jabber.core.asm.runtime` package
 */
object JabberClassLoader {
    private val lookup: MethodHandles.Lookup
    private val defined = hashMapOf<String, Class<*>>()

    init {
        val myLookup = MethodHandles.lookup()
        lookup = MethodHandles.privateLookupIn(LookupTarget::class.java, myLookup)
    }

    fun defineClass(node: ClassNode): Class<*> {
        return defined.getOrPut(node.name) { lookup.defineClass(node.byteArray()) }
    }
}
