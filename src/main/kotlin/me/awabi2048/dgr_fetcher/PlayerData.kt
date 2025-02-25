package me.awabi2048.dgr_fetcher

import me.awabi2048.dgr_fetcher.data_file.DataFile
import me.awabi2048.dgr_fetcher.misc.Lib
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player

class PlayerData(private val player: Player) {
    private val dataSection = DataFile.playerData.getConfigurationSection("${player.uniqueId}")

    init {
        if (!DataFile.playerData.contains("${player.uniqueId}")) {
            // initialize
            val initialData = mapOf(
                "quest_completed" to 0
            )

            DataFile.playerData.createSection("${player.uniqueId}", initialData)
            DataFile.save()
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

        init {
            if (!DataFile.playerData.contains("${player.uniqueId}.active_quests.${quest.id}")) {
                val activeQuestMap = quest.globalGoal?.keys?.associate { it to 0 }
                DataFile.playerData.createSection("${player.uniqueId}.active_quests.${quest.id}", activeQuestMap!!)
                DataFile.save()
            }
        }

        private val dataSection = DataFile.playerData.getConfigurationSection("${player.uniqueId}.active_quests.$id")

        var hasContributed: Boolean?
            get() {
                return dataSection?.getBoolean("has_contributed")
            }
            set(value) {
                if (value != null) {
                    DataFile.playerData.set("${player.uniqueId}.active_quests.$id.has_contributed", value)
                    DataFile.save()
                }
            }

//        var isCompleted: Boolean?
//            get() {
//                return dataSection?.getBoolean("is_completed")
//            }
//            set(value) {
//                if (value != null){
//                    DataFile.playerData.set("${player.uniqueId}.active_quests.$id.is_completed", value)
//                    DataFile.save()
//                }
//            }

        fun getContributionByMaterial(material: Material): Int? {
            return dataSection?.getInt(material.toString())
        }

        /**
         * @param amount 納品を試みるアイテム数。必要数を超える場合は、その値を返します。
         * @return 納品後の余剰アイテム数。
         */
        fun processContribution(amount: Int, material: Material): Int? {
            if (quest.isRegistered) {
                val currentContribution = getContributionByMaterial(material)!!
                val currentGlobalContribution = quest.getGlobalContributionByMaterial(material)!!
                val individualGoal = quest.individualGoal!![material]!!
                val globalGoal = quest.globalGoal!![material]!!

                val individualGoalReached =
                    currentContribution < individualGoal && currentContribution + amount >= individualGoal
                val globalGoalReached =
                    currentGlobalContribution < globalGoal && currentGlobalContribution + amount >= globalGoal

                // 次の到達点までの一時的なゴール。納品しすぎないようにする配慮です
                val goal = when (currentContribution) {
                    in 0..<individualGoal -> individualGoal
                    in individualGoal..<globalGoal -> globalGoal
                    else -> return amount // もう納品できない場合はここで返すので、以降は確実に納品したとしてよい
                }

                // 実際に消費される量を計算
                val consumeAmount = if (currentContribution + amount > goal) {
                    goal - currentContribution
                } else {
                    amount
                }

                val localizedName = Lib.resolveComponent(Component.translatable(material.translationKey()))

                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f)
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 2.0f)
                player.sendMessage("§6${localizedName}§7を§e${consumeAmount}個§7納品しました！")

                setContributionByMaterial(currentContribution + consumeAmount, material)

                // 新たに納品完了 →
                if (individualGoalReached) {
                    quest.individualGoalReached(player)
                }

                if (globalGoalReached) {
                    quest.globalGoalReached()
                }

                return amount - consumeAmount

            } else return null
        }

        private fun setContributionByMaterial(amount: Int, material: Material) {
            val path = "${player.uniqueId}.active_quests.${quest.id}.${material}"
            DataFile.playerData.set(path, amount)
            DataFile.save()
        }
    }
}
