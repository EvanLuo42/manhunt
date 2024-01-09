package cc.el42.manhunt

import cc.el42.manhunt.commands.ManhuntCommands
import cc.el42.manhunt.listeners.PlayerEventsListener
import cc.el42.manhunt.text.info
import cc.el42.manhunt.text.yellow
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {
    private val context = Context(plugin = this)

    override fun onEnable() {
        PlayerEventsListener(context)
        this.getCommand("manhunt")!!.setExecutor(ManhuntCommands(context))
        this.getCommand("manhunt")!!.tabCompleter = ManhuntCommands(context)
        this.getCommand("manhunt")!!.usage = """
            ${"/manhunt runner add [player]".yellow().toLegacyText()} ${"- Add a runner".info().toLegacyText()}
            ${"/manhunt runner remove [player]".yellow().toLegacyText()} ${"- Remove a runner".info().toLegacyText()}
            ${"/manhunt hunter add [player]".yellow().toLegacyText()} ${"- Add a hunter".info().toLegacyText()}
            ${"/manhunt hunter add [player]".yellow().toLegacyText()} ${"- Remove a hunter".info().toLegacyText()}
            ${"/manhunt start".yellow().toLegacyText()} ${"- Start a game".info().toLegacyText()}
        """.trimIndent()
    }
}