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

        if (event.clickedInventory?.any { it != null && it.itemMeta.persistentDataContainer.has(NamespacedKey(instance, "quest_id")) } == true) {
            event.isCancelled = true

            val questId =
                event.clickedInventory!!.find { it != null && it.itemMeta.persistentDataContainer.has(NamespacedKey(instance, "quest_id")) }?.itemMeta?.persistentDataContainer?.get(
                    NamespacedKey(instance, "quest_id"),
                    PersistentDataType.STRING
                )?: return

            val ui = QuestUI(event.whoClicked as Player, Quest(questId))
            ui.onClick(event)
        }
    }
}
