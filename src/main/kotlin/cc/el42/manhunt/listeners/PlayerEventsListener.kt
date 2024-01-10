package cc.el42.manhunt.listeners

import cc.el42.manhunt.Context
import cc.el42.manhunt.player.PlayerRole
import cc.el42.manhunt.text.failed
import cc.el42.manhunt.text.success
import cc.el42.manhunt.text.yellow
import net.md_5.bungee.api.ChatMessageType
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.meta.CompassMeta

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
        if (deadPlayer?.role != PlayerRole.RUNNER) return

        deadPlayer.dead = true
        deadPlayerEntity.gameMode = GameMode.SPECTATOR

        if (context.players
            .filter{ it.value.role == PlayerRole.RUNNER }
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

    @EventHandler
    fun onPlayerDropCompass(dropItemEvent: PlayerDropItemEvent) {
        if (!context.isGaming) return
        if (dropItemEvent.itemDrop.itemStack.type != Material.COMPASS) return
        if (context.players[dropItemEvent.player.uniqueId]?.role != PlayerRole.HUNTER) return
        dropItemEvent.isCancelled = true
    }

    @EventHandler
    fun onPlayerHeldCompass(itemHeldEvent: PlayerItemHeldEvent) {
        if (!context.isGaming) return
        if (itemHeldEvent.player.inventory.itemInMainHand.type != Material.COMPASS) return
        val trackingLocation =
            (itemHeldEvent.player.inventory.itemInMainHand.itemMeta as CompassMeta).lodestone ?: return
        val distance = itemHeldEvent.player.location.distance(trackingLocation)
        itemHeldEvent.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, "You are ".yellow().apply {
            this.addExtra("$distance".success())
            this.addExtra(" blocks from ")
        })
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
            .filter { it.value.role == PlayerRole.RUNNER }
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
            .filter { it.value.role == PlayerRole.HUNTER }
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