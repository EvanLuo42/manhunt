package cc.el42.manhunt.commands.runner

import cc.el42.manhunt.Context
import cc.el42.manhunt.player.PlayerData
import cc.el42.manhunt.player.PlayerRole
import cc.el42.manhunt.text.failed
import cc.el42.manhunt.text.success
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class RunnerCommand(private val context: Context): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size < 2) {
            sender.spigot().sendMessage("You should have at least 2 arguments.".failed())
            return false
        }
        return when (args[0]) {
            "add" -> addRunner(sender, args[1])
            "remove" -> removeRunner(sender, args[1])
            else -> false
        }
    }

    private fun addRunner(sender: CommandSender, playerName: String): Boolean {
        Bukkit.getPlayer(playerName)?.let {
            if (context.players.containsKey(it.uniqueId)) return false
            context.players[it.uniqueId] = PlayerData(PlayerRole.RUNNER)
            sender.spigot().sendMessage("You added $playerName as a runner".success())
            return true
        }
        sender.spigot().sendMessage("The player name you provided is not available".failed())
        return false
    }

    private fun removeRunner(sender: CommandSender, playerName: String): Boolean {
        Bukkit.getPlayer(playerName)?.let {
            if (!context.players.containsKey(it.uniqueId)) return false
            context.players.remove(it.uniqueId)
            sender.spigot().sendMessage("You removed $playerName from the runner team".success())
            return true
        }
        sender.spigot().sendMessage("The player name you provided is not available".failed())
        return false
    }
}