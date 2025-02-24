package me.awabi2048.dgr_fetcher

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: JavaPlugin
    }

    override fun onEnable() {
        instance = this

        // Plugin startup logic
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
