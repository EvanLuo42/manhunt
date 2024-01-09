package cc.el42.manhunt.text

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent

fun String.config(type: TextType, config:(TextComponent) -> Unit = {}): TextComponent {
    val text = TextComponent(this)
    when (type) {
        TextType.Success -> {
            text.apply {
                color = ChatColor.GREEN
            }.also {
                config(it)
            }
        }

        TextType.Failed -> {
            text.apply {
                color = ChatColor.RED
            }.also {
                config(it)
            }
        }
        TextType.Warn -> {
            text.apply {
                color = ChatColor.YELLOW
            }.also {
                config(it)
            }
        }
        TextType.Info -> {
            text.apply {
                isBold = false
            }.also {
                config(it)
            }
        }
        TextType.Custom -> {
            config(text)
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

fun String.yellow(): TextComponent {
    return this.config(TextType.Custom) {
        it.color = ChatColor.YELLOW
    }
}

fun String.success(): TextComponent {
    return this.config(TextType.Success)
}

fun String.failed(config: (TextComponent) -> Unit): TextComponent {
    return this.config(TextType.Failed, config)
}

fun String.failed(): TextComponent {
    return this.failed(config = {})
}