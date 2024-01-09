package cc.el42.manhunt

import cc.el42.manhunt.player.PlayerData
import cc.el42.manhunt.utils.Timer
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

data class Context(
    val players: MutableMap<UUID, PlayerData> = mutableMapOf(),
    var isGaming: Boolean = false,
    val plugin: JavaPlugin,
    val timer: Timer = Timer()
)
