package cc.el42.manhunt.commands

import cc.el42.manhunt.Context
import cc.el42.manhunt.commands.hunter.HunterCommand
import cc.el42.manhunt.commands.runner.RunnerCommand
import cc.el42.manhunt.text.failed
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class ManhuntCommands(private val context: Context): CommandExecutor, TabCompleter {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) {
            sender.spigot().sendMessage("Only human player can use this command.".failed())
            return true
        }

        if (args.isEmpty()) {
            return false
        }

        return when (args[0]) {
            "runner" -> RunnerCommand(context).onCommand(sender, command, label, args.drop(1).toTypedArray())
            "hunter" -> HunterCommand(context).onCommand(sender, command, label, args.drop(1).toTypedArray())
            "start" -> StartCommand(context).onCommand(sender, command, label, emptyArray())
            else -> false
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        return when (args.size) {
            1 -> mutableListOf("hunter", "runner", "start")
            2 -> when (args[1]) {
                "hunter", "runner" -> mutableListOf("add", "remove")
                else -> null
            }
            else -> null
        }
    }
}