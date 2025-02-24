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

    var totalContributed: Int?
        get() {
            return dataSection?.getInt("total_contributed")
        }
        set(value) {
            DataFile.playerData.set("${player.uniqueId}.total_contributed", value!!.coerceAtLeast(0))
        }

    var questCompleted: Int?
        get() {
            return dataSection?.getInt("quest_completed")
        }
        set(value) {
            DataFile.playerData.set("${player.uniqueId}.quest_completed", value!!.coerceAtLeast(0))
        }

    fun getQuestData(quest: Quest): PlayerQuestData {
        return PlayerQuestData(player, quest)
    }

    class PlayerQuestData(val player: Player, val quest: Quest) {
        private val id = quest.id
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

        fun processContribution(amount: Int, material: Material): Int? {
            if (quest.isRegistered){
                val currentContribution = getContributionByMaterial(material)!!
                val goal = quest.globalGoal!![material]!!

                if (currentContribution + amount > goal) {
                    setContributionByMaterial(goal, material)
                    return currentContribution + amount - goal
                } else {
                    setContributionByMaterial(currentContribution + amount, material)
                    return 0
                }

            } else return null
        }

        private fun setContributionByMaterial(amount: Int, material: Material) {

        }
    }
}
