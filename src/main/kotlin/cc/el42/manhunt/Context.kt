package cc.el42.manhunt

import cc.el42.manhunt.player.PlayerData
import cc.el42.manhunt.player.PlayerRole
import cc.el42.manhunt.utils.Timer
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

data class Context(
    val players: MutableMap<UUID, PlayerData> = mutableMapOf(),
    var isGaming: Boolean = false,
    val plugin: JavaPlugin,
    val timer: Timer = Timer()
)

fun MutableMap<UUID, PlayerData>.hunters(): MutableMap<UUID, PlayerData> {
    return this.filter { it.value.role == PlayerRole.HUNTER }.toMutableMap()
}

fun MutableMap<UUID, PlayerData>.runners(): MutableMap<UUID, PlayerData> {
    return this.filter { it.value.role == PlayerRole.RUNNER }.toMutableMap()
}
