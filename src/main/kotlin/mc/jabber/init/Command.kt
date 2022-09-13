package mc.jabber.init

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import mc.jabber.ComputerNetworks
import mc.jabber.minecraft.items.ChipItem
import mc.jabber.util.assertType
import net.minecraft.command.CommandBuildContext
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.ServerCommandSource
import org.quiltmc.qsl.command.api.CommandRegistrationCallback
import java.util.concurrent.CompletableFuture

object Command {
    fun register() {
        CommandRegistrationCallback.EVENT.register { commandDispatcher, commandBuildContext, registrationEnvironment ->
            addParamCommand(commandDispatcher)
            addNetworkDump(commandDispatcher)
        }
    }

    private fun addParamCommand(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(literal<ServerCommandSource>("jabberChipParam")
            .then(literal<ServerCommandSource>("long")
                .then(argument("key", StringArgumentType.string())
                    .suggests {context, builder -> getChipParamsOfType<Long>(context, builder)}
                    .then(argument("value", LongArgumentType.longArg())
                        .executes {
                            it.source.player.mainHandStack.getOrCreateSubNbt("params").putLong(
                                StringArgumentType.getString(it, "key"),
                                LongArgumentType.getLong(it, "value")
                            ); 1}))))
    }

    private fun addNetworkDump(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(literal<ServerCommandSource>("jabberNetworks")
            .executes {
                ComputerNetworks.networks.forEach { (id, list) ->
                    println("$id")
                    list.forEachIndexed { index, network ->
                        println("\t- $index")
                        network.locations.forEach {
                            println("\t\t - ${it.toShortString()}")
                        }
                    }
                }
                println("Done")
                1
            })
    }

    private inline fun <reified T: Any> getChipParamsOfType(context: CommandContext<ServerCommandSource>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        val stack = context.source.player.mainHandStack
        return if (stack.item is ChipItem) {
            val params = stack.item.assertType<ChipItem>().process.params
            when (T::class) {
                Long::class -> params.longParams
                else -> throw IllegalStateException("Unknown type ${T::class}")
            }.forEach { (key, _) -> builder.suggest(key) }
            builder.buildFuture()
        } else {
            builder.suggest("ILLEGAL ITEM TYPE")
            builder.buildFuture()
        }
    }
}
