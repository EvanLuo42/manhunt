package cc.el42.manhunt.text

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent

fun String.config(type: TextType): TextComponent {
    val text = TextComponent(this)
    when (type) {
        TextType.Success -> {
            text.apply {
                color = ChatColor.GREEN
            }
        }

        TextType.Failed -> {
            text.apply {
                color = ChatColor.RED
            }
        }
        TextType.Warn -> {
            text.apply {
                color = ChatColor.YELLOW
            }
        }
        TextType.Info -> {
            text.apply {
                isBold = false
            }
        }
        TextType.Custom -> {}
    }
    return text
}

fun String.info(): TextComponent {
    return this.config(TextType.Info)
}

fun String.warn(): TextComponent {
    return this.config(TextType.Warn)
}

fun String.yellow(): TextComponent {
    return this.config(TextType.Custom).apply {
        this.color = ChatColor.YELLOW
    }
}

fun String.success(): TextComponent {
    return this.config(TextType.Success)
}

fun String.failed(): TextComponent {
    return this.config(TextType.Failed)
}