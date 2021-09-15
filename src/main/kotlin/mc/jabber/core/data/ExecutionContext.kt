package mc.jabber.core.data

import net.minecraft.entity.LivingEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

data class ExecutionContext(var world: ServerWorld, var blockPos: BlockPos, var entity: LivingEntity?)
