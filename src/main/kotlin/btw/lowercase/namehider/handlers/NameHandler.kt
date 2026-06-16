package btw.lowercase.namehider.handlers

import btw.lowercase.namehider.config.NameHiderConfig
import btw.lowercase.namehider.config.SuffixBackupType
import btw.lowercase.namehider.util.DictionaryUtil
import btw.lowercase.namehider.util.GameUtil
import net.minecraft.client.network.NetworkPlayerInfo

object NameHandler {
    private var lastId = 0
    private val playerSuffixMap = HashMap<NetworkPlayerInfo, String>()
    private var localPlayerSuffix: String = ""

    @JvmStatic
    fun getNickname(info: NetworkPlayerInfo): String {
        val prefix = getPrefix()
        val suffix = getSuffix(info)
        return "$prefix-$suffix"
    }

    private fun getPrefix(): String {
        return NameHiderConfig.INSTANCE.namePrefix.trim().ifEmpty { "Player" }
    }

    private fun getSuffix(info: NetworkPlayerInfo): String {
        val definedSuffix = if (info == GameUtil.localPlayerInfo) localPlayerSuffix else this.getPlayerSuffix(info)
        return if (!definedSuffix.isEmpty()) {
            definedSuffix
        } else {
            NameHiderConfig.INSTANCE.nameSuffix.trim().ifEmpty { "unknown" }
        }
    }

    @JvmStatic
    fun assignPlayerSuffix(info: NetworkPlayerInfo) {
        val nameSuffix = NameHiderConfig.INSTANCE.nameSuffix.trim()
        val suffix = nameSuffix.ifEmpty {
            when (NameHiderConfig.INSTANCE.suffixBackupType()) {
                SuffixBackupType.COUNTER -> "${lastId++}"
                SuffixBackupType.RANDOM_WORD -> DictionaryUtil.random()
            }
        }

        if (info == GameUtil.localPlayerInfo) {
            localPlayerSuffix = suffix
        } else {
            this.playerSuffixMap[info] = suffix
        }
    }

    @JvmStatic
    fun reloadSuffixes() {
        assignPlayerSuffix(GameUtil.localPlayerInfo)
        for (entry in this.playerSuffixMap) {
            assignPlayerSuffix(entry.key)
        }
    }

    @JvmStatic
    fun clearPlayerSuffixes() {
        this.lastId = 0
        this.playerSuffixMap.clear()
        this.localPlayerSuffix = ""
    }

    @JvmStatic
    fun getPlayerSuffix(info: NetworkPlayerInfo): String {
        return this.playerSuffixMap[info] ?: ""
    }
}