package simulation

import mc.jabber.core.chips.action.AddChip
import mc.jabber.core.chips.constant.Constant0Chip
import mc.jabber.core.chips.constant.Constant1Chip
import mc.jabber.core.chips.logic.AndChip
import mc.jabber.core.chips.logic.OrChip
import mc.jabber.core.chips.special.CustomChip
import mc.jabber.core.circuit.CircuitManager
import net.minecraft.util.Identifier
import org.junit.jupiter.api.Test

class ChipTest {
    @Test
    fun testAddChip() {
        CircuitManager(3, 1).also {
            it.board[0, 0] = Constant1Chip()
            it.board[1, 0] = AddChip(5)
            it.board[2, 0] = CustomChip(Identifier("jabber:a")) { data, _, _ ->
                assert(data.acquire()?.second?.equals(6L) ?: false)
                data.empty()
            }
            it.setup()
            repeat(10) { _ ->
                it.simulate()
            }
        }

        CircuitManager(1, 3).also {
            it.board[0, 0] = Constant0Chip()
            it.board[0, 1] = AddChip(2)
            it.board[0, 2] = CustomChip(Identifier("jabber:a")) { data, _, _ ->
                assert(data.acquire()?.second?.equals(2L) ?: false)
                data.empty()
            }
            it.setup()
            repeat(10) { _ ->
                it.simulate()
            }
        }
    }

    @Test
    fun testLogicalAndChip() {
        CircuitManager(3, 2).also {
            it.board[0, 0] = Constant1Chip()
            it.board[1, 0] = AndChip()
            it.board[1, 1] = Constant1Chip()
            it.board[2, 0] = CustomChip(Identifier("jabber:a")) { data, _, _ ->
                assert(data.acquire()?.second?.equals(1L) ?: false)
                data.empty()
            }
            it.setup()
            repeat(10) { _ ->
                it.simulate()
            }
        }

        CircuitManager(3, 2).also {
            it.board[0, 0] = Constant1Chip()
            it.board[1, 0] = AndChip()
            it.board[1, 1] = Constant0Chip()
            it.board[2, 0] = CustomChip(Identifier("jabber:a")) { data, _, _ ->
                assert(data.acquire()?.second?.equals(0L) ?: false)
                data.empty()
            }
            it.setup()
            repeat(10) { _ ->
                it.simulate()
            }
        }
    }

    @Test
    fun testLogicalOrChip() {
        CircuitManager(3, 2).also {
            it.board[0, 0] = Constant1Chip()
            it.board[1, 0] = OrChip()
            it.board[1, 1] = Constant1Chip()
            it.board[2, 0] = CustomChip(Identifier("jabber:a")) { data, _, _ ->
                assert(data.acquire()?.second?.equals(1L) ?: false)
                data.empty()
            }
            it.setup()
            repeat(10) { _ ->
                it.simulate()
            }
        }

        CircuitManager(3, 2).also {
            it.board[0, 0] = Constant1Chip()
            it.board[1, 0] = OrChip()
            it.board[1, 1] = Constant0Chip()
            it.board[2, 0] = CustomChip(Identifier("jabber:a")) { data, _, _ ->
                assert(data.acquire()?.second?.equals(1L) ?: false)
                data.empty()
            }
            it.setup()
            repeat(10) { _ ->
                it.simulate()
            }
        }

        CircuitManager(3, 2).also {
            it.board[0, 0] = Constant0Chip()
            it.board[1, 0] = OrChip()
            it.board[1, 1] = Constant0Chip()
            it.board[2, 0] = CustomChip(Identifier("jabber:a")) { data, _, _ ->
                assert(data.acquire()?.second?.equals(0L) ?: false)
                data.empty()
            }
            it.setup()
            repeat(10) { _ ->
                it.simulate()
            }
        }
    }
}
