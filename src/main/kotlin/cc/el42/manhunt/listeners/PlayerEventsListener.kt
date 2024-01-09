package cc.el42.manhunt.listeners

import cc.el42.manhunt.Context
import cc.el42.manhunt.player.PlayerRole
import cc.el42.manhunt.text.failed
import cc.el42.manhunt.text.success
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

class PlayerEventsListener(private val context: Context): Listener {
    init {
        with (context) {
            plugin.server.pluginManager.registerEvents(this@PlayerEventsListener, this.plugin)
        }
    }

    @EventHandler
    fun onPlayerDeath(entityDeathEvent: EntityDeathEvent) {
        if (entityDeathEvent.entity !is Player) return

        val deadPlayerEntity = entityDeathEvent.entity as Player

        if (!context.players.containsKey(deadPlayerEntity.uniqueId)) return
        val deadPlayer = context.players[deadPlayerEntity.uniqueId]
        if (deadPlayer?.role != PlayerRole.Runner) return

        deadPlayer.dead = true
        deadPlayerEntity.gameMode = GameMode.SPECTATOR

        if (context.players
            .filter{ it.value.role == PlayerRole.Runner }
            .filter { !it.value.dead }.isNotEmpty()) return

        hunterWin()
        stopGame()
    }

    @EventHandler
    fun onPlayerKilledEnderDragon(entityDeathEvent: EntityDeathEvent) {
        if (entityDeathEvent.entity !is EnderDragon) return

        runnerWin()
        stopGame()
    }

    private fun stopGame() {
        context.apply {
            isGaming = false
            players.clear()
            timer.stop()
        }
    }

    private fun runnerWin() {
        win(
            "YOU FAILED".failed().toLegacyText(),
            "YOU WON".success().toLegacyText(),
            "Runners Killed the Ender Dragon"
        )
    }

    private fun hunterWin() {
        win(
            "YOU WON".success().toLegacyText(),
            "YOU FAILED".failed().toLegacyText(),
            "All Runners Are Killed by Hunters"
        )
    }

    private fun win(hunterTitle: String, runnerTitle: String, subtitle: String) {
        context.players
            .filter { it.value.role == PlayerRole.Runner }
            .forEach { (uuid, _) ->
                val player = Bukkit.getPlayer(uuid)
                player?.sendTitle(
                    runnerTitle,
                    subtitle,
                    10,
                    70,
                    20
                )
                player?.gameMode = GameMode.SURVIVAL
            }

        context.players
            .filter { it.value.role == PlayerRole.Hunter }
            .forEach { (uuid, _) ->
                Bukkit.getPlayer(uuid)?.sendTitle(
                    hunterTitle,
                    subtitle,
                    10,
                    70,
                    20
                )
            }
        context.players.forEach { (uuid, _) ->
            Bukkit.getPlayer(uuid)?.spigot()?.sendMessage(
                "The game finished in ${context.timer.duration.inWholeMinutes} minutes".success()
            )
        }
    }
}