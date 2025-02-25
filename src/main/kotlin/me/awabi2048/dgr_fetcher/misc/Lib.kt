package me.awabi2048.dgr_fetcher.misc

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Lib {
    fun resolveComponent(component: Component): String {
        return PlainTextComponentSerializer.plainText().serialize(component)
    }

    fun getVirtualItem(material: Material): ItemStack {
        val item = ItemStack(material)
        item.editMeta{
            it.isHideTooltip = true
        }
        return item
    }
}
