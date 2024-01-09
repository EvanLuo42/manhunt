package cc.el42.manhunt.commands.runner

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class RunnerCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {

        }
        return when (args[0]) {
            "add" -> false
            else -> false
        }
    }
}