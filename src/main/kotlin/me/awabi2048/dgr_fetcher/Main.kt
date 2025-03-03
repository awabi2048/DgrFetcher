package me.awabi2048.dgr_fetcher

import me.awabi2048.dgr_fetcher.command.OpenQuestUICommand
import me.awabi2048.dgr_fetcher.data_file.DataFile
import me.awabi2048.dgr_fetcher.listener.QuestUIListener
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: JavaPlugin
        const val PREFIX = "§6DonguriFetcher §7»"
        const val OAGE_PREFIX = "§6おあげちゃん §7»"
        lateinit var economy: Economy
    }

//    private fun setupEconomy(): Boolean {
//        server.pluginManager.getPlugin("Vault")?: return false
//
//        val rsp = server.servicesManager.getRegistration(Economy::class.java)?: return false
//
//        economy = rsp.provider
//        return true
//    }

    override fun onEnable() {
        instance = this

        // コマンド
        getCommand("quest")?.setExecutor(OpenQuestUICommand)

        // リスナー
        server.pluginManager.registerEvents(QuestUIListener, instance)

        // データファイルをコピー
        DataFile.copy()
        DataFile.load()

        // vault
//        val economySetup = setupEconomy()
//        if (!economySetup) {
//            logger.severe("Setup failed. Disabling plugin.")
//            server.pluginManager.disablePlugins()
//            return
//        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
