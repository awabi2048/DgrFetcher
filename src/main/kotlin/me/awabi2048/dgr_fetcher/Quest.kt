package me.awabi2048.dgr_fetcher

import me.awabi2048.dgr_fetcher.data_file.DataFile
import me.awabi2048.dgr_fetcher.quest.QuestReward
import org.bukkit.Bukkit
import org.bukkit.Material

class Quest(val id: String) {
    private val dataSection = DataFile.questData.getConfigurationSection(id)

    val isRegistered: Boolean
        get() {
            return (dataSection != null)
        }

    val individualGoal: Map<Material, Int>?
        get() {
            return dataSection?.getConfigurationSection("individual_goal")?.getKeys(false)?.associate {
                Material.valueOf(it) to dataSection.getInt("individual_goal.$it")
            }
        }

    val globalGoal: Map<Material, Int>?
        get() {
            return dataSection?.getConfigurationSection("global_goal")?.getKeys(false)?.associate {
                Material.valueOf(it) to dataSection.getInt("global_goal.$it")
            }
        }

    val individualReward: Map<QuestReward, Int>?
        get() {
            return dataSection?.getConfigurationSection("individual_reward")?.getKeys(false)?.associate {
                QuestReward.valueOf(it.uppercase()) to dataSection.getInt("reward.individual.$it")
            }
        }

    val globalReward: Map<QuestReward, Int>?
        get() {
            return dataSection?.getConfigurationSection("global_reward")?.getKeys(false)?.associate {
                QuestReward.valueOf(it.uppercase()) to dataSection.getInt("reward.global.$it")
            }
        }

    val iconMaterial: Material?
        get() {
            return Material.valueOf(dataSection?.getString("icon") ?: return null)
        }

    fun getGlobalContributionByMaterial(material: Material): Int? {
        return if (isRegistered) DataFile.playerData.getKeys(false).sumOf {
            PlayerData(Bukkit.getOfflinePlayer(it).player!!).getQuestData(id).getContributionByMaterial(material)!!
        } else null
    }
}
