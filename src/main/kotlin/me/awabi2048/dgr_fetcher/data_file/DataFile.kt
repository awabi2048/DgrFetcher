package me.awabi2048.dgr_fetcher.data_file

import me.awabi2048.dgr_fetcher.Main.Companion.instance
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object DataFile {

    lateinit var playerData: FileConfiguration
    lateinit var questData: FileConfiguration
    lateinit var config: FileConfiguration

    val files = listOf(
        "player_data.yml",
        "quest_data.yml",
        "config.yml",
    )

    fun copy() {
        for (path in files) {
            files.forEach {
                if (!File(instance.dataFolder.path + File.separator + it).exists()) {
                    instance.saveResource(it, false)
                    instance.logger.info("DonguriFetcher >> Copied \"$it\" to the data folder.")
                }
            }
        }
    }

    fun load() {
        playerData = YamlUtil.load("player_data.yml")
        questData = YamlUtil.load("quest_data.yml")
        config = YamlUtil.load("config.yml")
    }

    fun save() {
        YamlUtil.save("player_data.yml", playerData as YamlConfiguration)
        YamlUtil.save("quest_data.yml", questData as YamlConfiguration)
        YamlUtil.save("config.yml", config as YamlConfiguration)
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
