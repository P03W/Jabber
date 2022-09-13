package mc.jabber

import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World

object ComputerNetworks {
    val networks = mutableMapOf<Identifier, MutableList<Network>>()

    fun joinNetwork(key: RegistryKey<World>, pos: BlockPos): Network {
        val networks = networks.computeIfAbsent(key.value) {
            mutableListOf()
        }

        val willAccept = mutableListOf<Network>()
        willAccept.addAll(networks.filter { it.willAccept(pos) })

        if (willAccept.isEmpty()) {
            val newNetwork = Network(key.value)
            newNetwork.join(pos)
            networks.add(newNetwork)
            return newNetwork
        }

        val network = if (willAccept.size == 1) {
            willAccept[0]
        } else {
            willAccept[0].mergeWith(willAccept.takeLast(willAccept.size - 2)).also {
                purgeNetworks()
            }
        }

        network.join(pos)
        networks.add(network)
        return network
    }

    fun isInNetwork(key: RegistryKey<World>, pos: BlockPos): Boolean {
        return networks[key.value]?.any { it.contains(pos) } == true
    }

    fun getNetwork(key: RegistryKey<World>, pos: BlockPos): Network? {
        return networks[key.value]?.first { it.contains(pos) }
    }

    fun leaveNetwork(key: RegistryKey<World>, pos: BlockPos) {
        getNetwork(key, pos)?.leave(pos)
    }

    private fun purgeNetworks() {
        networks.forEach { (_, list) ->
            val toRemove = list.filter { it.shouldBeRemoved }
            list.removeAll(toRemove)
        }
    }

    class Network(
        val worldId: Identifier,
    ) {
        val locations = mutableSetOf<BlockPos>()

        private var forceRemove = false
        val shouldBeRemoved = locations.size == 0 || forceRemove

        fun willAccept(pos: BlockPos): Boolean {
            return true // TODO
        }

        fun contains(pos: BlockPos): Boolean {
            return locations.contains(pos)
        }

        fun join(pos: BlockPos) {
            locations.add(pos)
            // TODO
        }

        fun leave(pos: BlockPos) {

        }

        fun mergeWith(other: List<Network>): Network {
            other.forEach {
                locations.addAll(it.locations)
                it.forceRemove = true
            }
            return this
        }
    }
}