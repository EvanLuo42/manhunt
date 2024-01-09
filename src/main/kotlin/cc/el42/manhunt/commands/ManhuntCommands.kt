package cc.el42.manhunt.commands

import cc.el42.manhunt.commands.hunter.HunterCommand
import cc.el42.manhunt.commands.runner.RunnerCommand
import cc.el42.manhunt.player.PlayerData
import cc.el42.manhunt.text.failed
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ManhuntCommands: CommandExecutor {
    companion object {
        val players: Map<Player, PlayerData> = emptyMap()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.spigot().sendMessage("Only human player can use this command.".failed())
            return false
        }
        if (args.isEmpty()) return false

        return when (args[0]) {
            "runner" -> RunnerCommand().onCommand(sender, command, label, args.drop(1).toTypedArray())
            "hunter" -> HunterCommand().onCommand(sender, command, label, args.drop(1).toTypedArray())
            "start" -> StartCommand().onCommand(sender, command, label, args.drop(1).toTypedArray())
            else -> false
        }
    }
}