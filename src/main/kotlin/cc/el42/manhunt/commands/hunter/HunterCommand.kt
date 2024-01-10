package cc.el42.manhunt.commands.hunter

import cc.el42.manhunt.Context
import cc.el42.manhunt.player.PlayerData
import cc.el42.manhunt.player.PlayerRole
import cc.el42.manhunt.text.failed
import cc.el42.manhunt.text.success
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

class HunterCommand(private val context: Context): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size < 2) {
            sender.spigot().sendMessage("You should have at least 2 arguments.".failed())
            return false
        }

        return when (args[0]) {
            "add" -> addHunter(sender, args[1])
            "remove" -> removeHunter(sender, args[1])
            else -> false
        }
    }

    private fun addHunter(sender: CommandSender, playerName: String): Boolean {
        Bukkit.getPlayer(playerName)?.let {
            if (context.players.containsKey(it.uniqueId)) return false
            context.players[it.uniqueId] = PlayerData(PlayerRole.HUNTER)
            it.inventory.setItem(0, ItemStack(Material.COMPASS))
            sender.spigot().sendMessage("You added $playerName as a hunter".success())
            return true
        }
        sender.spigot().sendMessage("The player name you provided is not available".failed())
        return false
    }

    private fun removeHunter(sender: CommandSender, playerName: String): Boolean {
        Bukkit.getPlayer(playerName)?.let {
            if (!context.players.containsKey(it.uniqueId)) return false
            context.players.remove(it.uniqueId)
            sender.spigot().sendMessage("You removed $playerName from the hunter team".success())
            return true
        }
        sender.spigot().sendMessage("The player name you provided is not available".failed())
        return false
    }
}