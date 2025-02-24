package me.awabi2048.dgr_fetcher.ui

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

abstract class AbstractUI(private val owner: Player) {

    val bar = "§7" + "━".repeat(30)
    val index = "§f§l|"

    val ui: Inventory
        get() {
            return construct()
        }
    abstract fun construct(): Inventory

    fun open() {
        owner.openInventory(ui)
    }
}
