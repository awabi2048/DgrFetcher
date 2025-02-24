package me.awabi2048.dgr_fetcher.ui

import me.awabi2048.dgr_fetcher.PlayerData
import me.awabi2048.dgr_fetcher.Quest
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemRarity
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt

class QuestUI(private val player: Player, private val quest: Quest): AbstractUI(player) {
    override fun construct(): Inventory {
        if (!quest.isRegistered) throw IllegalArgumentException("Unregistered quest given.")

        val requirementCount = quest.globalGoal!!.size
        val playerQuestData = PlayerData(player).getQuestData(quest.id)

        val ui = Bukkit.createInventory(null, requirementCount / 7 + 4, "§8§lQuest")

        for (material in quest.globalGoal!!.keys) {
            val icon = ItemStack(material)

            //
            val individualContribution = playerQuestData.getContributionByMaterial(material)!!
            val globalContribution = quest.getGlobalContributionByMaterial(material)!!
            val individualGoal = quest.individualGoal!![material]!!
            val globalGoal = quest.globalGoal!![material]!!

            val individualContributionRate = ((individualContribution.toDouble() / individualGoal) * 100).roundToInt()
            val globalContributionRate = ((globalContribution.toDouble() / globalGoal) * 100).roundToInt()
            val individualContributionRateToGlobal = ((individualContribution.toDouble() / globalGoal) * 100).roundToInt()

            //
            val individualContributionText = when(individualContribution >= individualGoal) {
                true -> "$index §7納品数 §d納品完了！"
                false -> "$index §7納品数 §6$individualContribution§7/$individualGoal (§e$individualContributionRate%§7)"
            }

            val globalContributionText = when(individualContribution >= individualGoal) {
                true -> "$index §7グローバル §a納品完了！"
                false -> "$index §7グローバル §6$globalContribution§7/$individualGoal §7(うち §e$individualContribution§7/$individualGoal §b$individualContributionRateToGlobal%§7)"
            }

            icon.itemMeta = icon.itemMeta.apply {
                setRarity(ItemRarity.RARE)
                lore = listOf(
                    bar,
                    individualContributionText,
                    bar,
                    globalContributionText,
                    bar,
                )
            }

            val index = quest.globalGoal!!.keys.indexOf(material)
            val row = index / 7 + 2
            val column = index % 7 + 1
            val slot = row * 9 + column

            ui.setItem(slot, icon)
        }

        return ui
    }
}
