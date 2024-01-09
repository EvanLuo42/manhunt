package cc.el42.manhunt.text

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent

fun String.config(type: TextType, config:(String) -> Unit = {}): TextComponent {
    val text = TextComponent(this)
    when (type) {
        TextType.Success -> {
            text.color = ChatColor.GREEN
            text.isBold = true
            config(this)
        }

        TextType.Failed -> {
            text.color = ChatColor.RED
            text.isBold = true
            config(this)
        }
        TextType.Warn -> {
            text.color = ChatColor.YELLOW
            text.isBold = true
            config(this)
        }
        TextType.Info -> {
            text.isBold = false
            config(this)
        }
        TextType.Custom -> {
            config(this)
        }
    }
    return text
}

fun String.info(): TextComponent {
    return this.config(TextType.Info)
}

fun String.warn(): TextComponent {
    return this.config(TextType.Warn)
}

fun String.success(): TextComponent {
    return this.config(TextType.Success)
}

fun String.failed(): TextComponent {
    return this.config(TextType.Failed)
}