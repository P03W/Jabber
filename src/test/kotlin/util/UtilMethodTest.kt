package util

import mc.jabber.util.PANIC
import mc.jabber.util.capitalize
import mc.jabber.util.error.CatchFireAndExplode
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UtilMethodTest {
    @Test
    fun testCapitalize() {
        assert(
            "this Is An Example string 22587 q9y4 87o9 9* a-= _+ ''' 'w2 3".capitalize() ==
                    "This Is An Example string 22587 q9y4 87o9 9* a-= _+ ''' 'w2 3")
    }

    @Test
    fun testCanPanic() {
        println("Making sure we can panic, don't worry about the next message")
        Assertions.assertThrows(CatchFireAndExplode::class.java) {
            PANIC()
        }
    }
}
