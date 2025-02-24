package me.awabi2048.dgr_fetcher.data_file

import me.awabi2048.dgr_fetcher.Main.Companion.instance
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object DataFile {

    lateinit var playerData: FileConfiguration
    lateinit var questData: FileConfiguration
    lateinit var config: FileConfiguration

    lateinit var dataMap: MutableMap<String, FileConfiguration>

    val files = listOf(
        "player_data.yml",
        "quest_data.yml",
        "config.yml",
    )

    fun copy() {
//        for (path in files) {
//
//        }
    }

    fun load() {
        for (path in files) {
            dataMap[path]?.load(path)
        }

        playerData.load("player_data.yml")
        questData.load("quest_data.yml")
        config.load("config.yml")
    }

    fun save() {
        playerData.save("player_data.yml")
        playerData.save("player_data.yml")
        playerData.save("player_data.yml")
    }

    object YamlUtil {
        fun load(filePath: String): YamlConfiguration {
            val settingDataFile = File(instance.dataFolder.toString() + File.separator + filePath.replace("/", File.separator))
            return YamlConfiguration.loadConfiguration(settingDataFile)
        }

        fun save(filePath: String, yamlSection: YamlConfiguration): Boolean {
            try {
                val settingDataFile =
                    File(instance.dataFolder.toString() + File.separator + filePath.replace("/", File.separator))
                yamlSection.save(settingDataFile)

                return true
            } catch (e: Exception) {
                return false
            }
        }
    }
}
