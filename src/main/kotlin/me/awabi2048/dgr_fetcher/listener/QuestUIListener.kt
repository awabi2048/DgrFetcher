package me.awabi2048.dgr_fetcher.listener

import me.awabi2048.dgr_fetcher.Main.Companion.instance
import me.awabi2048.dgr_fetcher.Quest
import me.awabi2048.dgr_fetcher.ui.QuestUI
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.persistence.PersistentDataType

object QuestUIListener : Listener {
    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory?.any { it.itemMeta.itemName == "［納品する］" } == true) {
            val questId =
                event.clickedInventory!!.find { it.itemMeta.itemName == "［納品する］" }?.itemMeta?.persistentDataContainer?.get(
                    NamespacedKey(instance, "gui_item"),
                    PersistentDataType.STRING
                )?: return

            val ui = QuestUI(event.whoClicked as Player, Quest(questId))
            ui.onClick(event)
        }
    }
}