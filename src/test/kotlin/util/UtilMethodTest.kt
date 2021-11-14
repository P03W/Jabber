package util

import mc.jabber.util.capitalize
import org.junit.jupiter.api.Test

class UtilMethodTest {
    @Test
    fun testCapitalize() {
        assert(
            "this Is An Example string 22587 q9y4 87o9 9* a-= _+ ''' 'w2 3".capitalize() ==
                    "This Is An Example string 22587 q9y4 87o9 9* a-= _+ ''' 'w2 3")
    }
}
