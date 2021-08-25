package mc.jabber.util

import mc.jabber.util.error.CatchFireAndExplode
import org.apache.logging.log4j.LogManager

@Suppress("FunctionName")
fun PANIC(): Nothing {
    LogManager.getLogger("Jabber | Panic").fatal("IMPOSSIBLE CONDITION REACHED?!")
    throw CatchFireAndExplode()
}
