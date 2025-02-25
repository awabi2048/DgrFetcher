package me.awabi2048.dgr_fetcher.misc

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

object Lib {
    fun resolveComponent(component: Component): String {
        return PlainTextComponentSerializer.plainText().serialize(component)
    }
}
