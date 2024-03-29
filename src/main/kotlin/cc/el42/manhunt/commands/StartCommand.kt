package cc.el42.manhunt.commands

import cc.el42.manhunt.Context
import cc.el42.manhunt.hunters
import cc.el42.manhunt.runners
import cc.el42.manhunt.text.failed
import cc.el42.manhunt.text.success
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.inventory.meta.CompassMeta
import java.util.*

class StartCommand(private val context: Context): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (context.isGaming) {
            sender.spigot().sendMessage("You are still in a game".failed())
            context.plugin.logger.warning("Game exist")
            return true
        }

        if (context.players.hunters().isEmpty() || context.players.runners().isEmpty()) {
            sender.spigot().sendMessage("You should have at least 1 hunter and runner".failed())
            context.plugin.logger.warning("Hunter or runner not enough")
            return true
        }

        context.apply {
            isGaming = true
            timer.stop()
            timer.start()
            players.hunters().forEach { initializeHunterCompass(it.key) }
        }

        sender.spigot().sendMessage("Game Start!".success())

        return true
    }

    private fun initializeHunterCompass(uuid: UUID) {
        Bukkit.getPlayer(uuid)?.inventory?.forEach itemForEach@ { itemStack ->
            if (itemStack.type != Material.COMPASS) return@itemForEach
            (itemStack.itemMeta as CompassMeta).lodestone = Bukkit.getPlayer(context.players
                .runners()
                .keys
                .toList()
                .first()
            )?.location
        }
    }
}