package me.awabi2048.dgr_fetcher

import me.awabi2048.dgr_fetcher.command.OpenQuestUICommand
import me.awabi2048.dgr_fetcher.data_file.DataFile
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: JavaPlugin
        const val PREFIX = "§7«§6DgrFetcher§7»"
    }

    override fun onEnable() {
        instance = this

        // コマンド
        getCommand("quest")?.setExecutor(OpenQuestUICommand)

        // データファイルをコピー
        DataFile.copy()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
