package me.awabi2048.dgr_fetcher

import me.awabi2048.dgr_fetcher.data_file.DataFile
import org.bukkit.Material
import org.bukkit.entity.Player

class PlayerData(private val player: Player) {
    private val dataSection = DataFile.playerData.getConfigurationSection("${player.uniqueId}")

    init {
        if (dataSection == null) {
            // initialize
            val initialData = mapOf(
                "quest_completed" to 0
            )

            DataFile.playerData.createSection("${player.uniqueId}", initialData)
        }
    }

    fun getQuestData(id: String): PlayerQuestData {
        return PlayerQuestData(player, id)
    }

    class PlayerQuestData(val player: Player, val id: String) {
        private val dataSection = DataFile.playerData.getConfigurationSection("${player.uniqueId}.active_quests.$id")

        var isCompleted: Boolean?
            get() {
                return dataSection?.getBoolean("is_completed")
            }
            set(value) {
                DataFile.playerData.set("${player.uniqueId}.active_quests.$id", value)
                DataFile.save()
            }

        fun getContributionByMaterial(material: Material): Int? {
            return dataSection?.getInt(material.toString())
        }
    }
}
