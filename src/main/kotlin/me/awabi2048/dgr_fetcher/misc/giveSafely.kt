package me.awabi2048.dgr_fetcher.misc

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun Player.giveSafely(item: ItemStack) {
    val inventory = player!!.inventory
    if (inventory.firstEmpty() == 35) {

    }
}