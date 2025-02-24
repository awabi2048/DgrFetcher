package me.awabi2048.dgr_fetcher.ui

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

abstract class AbstractInteractiveUI(owner: Player): AbstractUI(owner) {
    /**
     * @return クリックイベントをキャンセルするかどうか
     */
    abstract fun onClick(event: InventoryClickEvent)
}
