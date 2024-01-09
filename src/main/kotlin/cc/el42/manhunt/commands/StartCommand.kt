package cc.el42.manhunt.commands

import cc.el42.manhunt.Context
import cc.el42.manhunt.player.PlayerRole
import cc.el42.manhunt.text.failed
import cc.el42.manhunt.text.success
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class StartCommand(private val context: Context): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (context.isGaming) {
            sender.spigot().sendMessage("You are still in a game".failed())
            context.plugin.logger.warning("Game exist")
            return true
        }
        if (context.players.filter { it.value.role == PlayerRole.Hunter }.isEmpty()
            || context.players.filter { it.value.role == PlayerRole.Runner }.isEmpty()) {
            sender.spigot().sendMessage("You should have at least 1 hunter and runner".failed())
            context.plugin.logger.warning("Hunter or runner not enough")
            return true
        }

        context.apply {
            isGaming = true
            timer.stop()
            timer.start()
        }

        sender.spigot().sendMessage("Game Start!".success())

        return true
    }
}