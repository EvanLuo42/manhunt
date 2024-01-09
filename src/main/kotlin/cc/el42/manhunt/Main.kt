package cc.el42.manhunt

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {
    override fun onEnable() {
        Bukkit.getLogger().info("ABC")
    }
}