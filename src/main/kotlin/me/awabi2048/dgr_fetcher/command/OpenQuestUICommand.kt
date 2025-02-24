package me.awabi2048.dgr_fetcher.command

import me.awabi2048.dgr_fetcher.Main.Companion.PREFIX
import me.awabi2048.dgr_fetcher.Quest
import me.awabi2048.dgr_fetcher.ui.QuestUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

object OpenQuestUICommand: CommandExecutor, TabCompleter {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (p0 !is Player) {
            p0.sendMessage("DgrFetcher >>")
            return true
        }

        if (p3?.size != 1) {
            p0.sendMessage("$PREFIX §c無効なコマンドです。")
            return true
        }

        val questId = p3[0]
        val quest = Quest(questId)

        if (!quest.isRegistered) {
            p0.sendMessage("$PREFIX §c無効なクエスト指定です。")
            return true
        }

        val ui = QuestUI(p0, quest)
        ui.open()

        return true
    }

    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>?,
    ): MutableList<String>? {
        return null
    }
}
