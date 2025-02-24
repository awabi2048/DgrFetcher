package me.awabi2048.dgr_fetcher

import me.awabi2048.dgr_fetcher.Main.Companion.OAGE_PREFIX
import me.awabi2048.dgr_fetcher.Main.Companion.economy
import me.awabi2048.dgr_fetcher.Main.Companion.instance
import me.awabi2048.dgr_fetcher.data_file.DataFile
import me.awabi2048.dgr_fetcher.quest.QuestReward
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player

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
            PlayerData(Bukkit.getOfflinePlayer(it).player!!).getQuestData(this).getContributionByMaterial(material)!!
        } else null
    }

    fun distributeIndividualReward(player: Player) {
        if (!isRegistered) return
        if (!player.isOnline) return

        val donguri = individualReward!![QuestReward.DONGURI]!!
        val expPoint = individualReward!![QuestReward.EXP_POINT]!!

        Bukkit.getScheduler().runTaskLater(
            instance,
            Runnable {
                Donguri.give(player, donguri)
                player.exp += expPoint
            },
            20L
        )

        // TODO: お礼メッセージ
        player.sendMessage("$OAGE_PREFIX §fお礼の§6どんぐり§fです！ありがとー！")
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f)
    }
}
