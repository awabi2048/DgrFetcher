package me.awabi2048.dgr_fetcher

import me.awabi2048.dgr_fetcher.Main.Companion.economy
import org.bukkit.Sound
import org.bukkit.entity.Player

//object Donguri {
//    fun getFormated(amount: Int): String {
//        return " §e\uD83D\uDC3F $amount "
//    }
//
//    fun give(player: Player, amount: Int) {
//        economy.depositPlayer(player, amount.toDouble())
//
//        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f)
//        player.sendMessage("§7どんぐり${getFormated(amount)}§7を受け取りました！")
//    }
//
//    fun take(player: Player, amount: Int) {
//        economy.withdrawPlayer(player, amount.toDouble())
//    }
//
//    fun getAmount(player: Player): Int {
//        return economy.getBalance(player).toInt()
//    }
//}
