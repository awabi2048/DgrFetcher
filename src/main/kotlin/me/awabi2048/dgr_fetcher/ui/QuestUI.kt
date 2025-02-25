package me.awabi2048.dgr_fetcher.ui

import me.awabi2048.dgr_fetcher.Main.Companion.instance
import me.awabi2048.dgr_fetcher.PlayerData
import me.awabi2048.dgr_fetcher.Quest
import me.awabi2048.dgr_fetcher.misc.Lib
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemRarity
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.math.roundToInt

class QuestUI(private val player: Player, private val quest: Quest) : AbstractInteractiveUI(player) {
    override fun onClick(event: InventoryClickEvent) {

        if (!quest.isRegistered) throw IllegalArgumentException("Unregistered quest given.")
        event.isCancelled = true

        // 納品
        val fetchSlot = event.clickedInventory!!.indexOfLast { it != null && it.itemMeta.itemName == "§b［納品する］" }

        if (event.slot != fetchSlot) return
        if (event.cursor.type !in quest.globalGoal!!.keys) return

        val player = event.whoClicked as Player

        // 納品個数更新
        val playerData = PlayerData(player).getQuestData(quest)
        val remained = playerData.processContribution(event.cursor.amount, event.cursor.type)!!

        println("remained: $remained")

        //
        event.setCursor(event.cursor.apply { amount = remained })

        open()

        return
    }

    override fun construct(): Inventory {

        fun slotOf(index: Int): Int {
            val row = index / 7 + 2
            val column = index % 7 + 1
            val slot = row * 9 + column

            return slot
        }

        if (!quest.isRegistered) throw IllegalArgumentException("Unregistered quest given.")

        val requirementTypes = quest.globalGoal!!.size
        val playerQuestData = PlayerData(player).getQuestData(quest)
        val playerData = PlayerData(player)

        val ui = Bukkit.createInventory(null, (requirementTypes / 7 + 4) * 9, "§8§lQuest")

        var individualContributionRateTotal = 0
        var globalContributionRateTotal = 0

        // 納品素材の表示
        for (material in quest.globalGoal!!.keys) {
            val icon = ItemStack(material)

            // 納品数・目標数を取得
            val individualContribution = playerQuestData.getContributionByMaterial(material)!!
            val globalContribution = quest.getGlobalContributionByMaterial(material)!!
            val individualGoal = quest.individualGoal!![material]!!
            val globalGoal = quest.globalGoal!![material]!!

            // 目標に対する割合をパーセントで取得
            val individualContributionRate = ((individualContribution.toDouble() / individualGoal) * 100).roundToInt()
            val globalContributionRate = ((globalContribution.toDouble() / globalGoal) * 100).roundToInt()
            val individualContributionRateToGlobal =
                ((individualContribution.toDouble() / globalGoal) * 100).roundToInt()

            // 総計達成率
            individualContributionRateTotal += (individualContributionRate.toDouble() / quest.globalGoal!!.size).roundToInt()
            globalContributionRateTotal += (globalContributionRate.toDouble() / quest.globalGoal!!.size).roundToInt()

            // 達成しているかどうかでテキスト変える
            val individualContributionText = when (individualContribution >= individualGoal) {
                true -> "$index §7納品数 §d納品完了！"
                false -> "$index §7納品数 §6$individualContribution§7/$individualGoal (§e$individualContributionRate%§7)"
            }

            val globalContributionText = when (globalContribution >= globalGoal) {
                true -> "$index §7グローバル §a納品完了！"
                false -> "$index §7グローバル §6$globalContribution§7/$globalGoal §7(§e$globalContributionRate%§7)、§7うち §e$individualContribution§7/$globalGoal (§e$individualContributionRateToGlobal%§7)"
            }

            // アイテムのmetaに書き込み
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

            // slotを計算、配置
            val index = quest.globalGoal!!.keys.indexOf(material)
            ui.setItem(slotOf(index), icon)
        }

        // あと何個の表示
        val remainingItems = quest.globalGoal!!.keys.map {
            val requirementCount = quest.globalGoal!![it]!!
            val playerContribution = PlayerData(player).getQuestData(quest).getContributionByMaterial(it)!!

            val localizedName = Component.translatable(it.translationKey()).compact()

            when (playerContribution >= requirementCount) {
                true -> "$index §7$localizedName §e納品完了"
                false -> "$index §7$localizedName §fあと§e${requirementCount - playerContribution}個"
            }
        }

        // 中央・納品アイコン
        val fetchIcon = ItemStack(Material.CHEST)
        fetchIcon.itemMeta = fetchIcon.itemMeta.apply {
            setItemName("§b［納品する］")
            lore = listOf(
                bar,
                "§7ここにアイテムを§eドラッグ§7して、納品を行います。",
                "§c一度納品したアイテムは取り出せません。",
                bar,
            ) + remainingItems + bar

            persistentDataContainer.set(
                NamespacedKey(instance, "quest_id"),
                PersistentDataType.STRING,
                quest.id
            )
        }

        // 左・プレイヤープロフィール
        val playerIcon = ItemStack(Material.PLAYER_HEAD)
        playerIcon.itemMeta = playerIcon.itemMeta.apply {
            setItemName("§aあなたのプロフィール")
            lore = listOf(
                bar,
                "$index §7納品したアイテム数 ${playerData.totalContributed}",
                "$index §7完了させたクエスト ${playerData.questCompleted}",
                bar,
            )
        }

        val globalIcon = ItemStack(Material.GRASS_BLOCK)

        // 設置: slotはUIのサイズに合わせて、後ろから◯番目
        ui.setItem(ui.size - 7, playerIcon)
        ui.setItem(ui.size - 5, fetchIcon)
        ui.setItem(ui.size -3, globalIcon)

        return ui
    }
}
