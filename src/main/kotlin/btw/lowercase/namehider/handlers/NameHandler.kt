package btw.lowercase.namehider.handlers

import btw.lowercase.namehider.config.NameHiderConfig
import btw.lowercase.namehider.config.SuffixBackupType
import btw.lowercase.namehider.util.DictionaryUtil
import net.minecraft.client.network.NetworkPlayerInfo
import java.util.*

object NameHandler {
    private var lastId = 0
    private val playerSuffixMap = HashMap<UUID, String>()

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
        val empty = NameHiderConfig.INSTANCE.nameSuffix.isEmpty()
        val suffix = getPlayerSuffix(info)
        return if (!suffix.isEmpty()) {
            suffix
        } else if (empty) {
            "unknown"
        } else {
            NameHiderConfig.INSTANCE.nameSuffix
        }
    }

    @JvmStatic
    fun assignPlayerSuffix(info: NetworkPlayerInfo) {
        val nameSuffix = NameHiderConfig.INSTANCE.nameSuffix.trim()
        playerSuffixMap[info.gameProfile.id] = nameSuffix.ifEmpty {
            when (NameHiderConfig.INSTANCE.suffixBackupType()) {
                SuffixBackupType.COUNTER -> "${lastId++}"
                SuffixBackupType.RANDOM_WORD -> DictionaryUtil.random()
            }
        }
    }

    @JvmStatic
    fun clearPlayerSuffixes() {
        lastId = 0
        playerSuffixMap.clear()
    }

    @JvmStatic
    fun getPlayerSuffix(info: NetworkPlayerInfo): String {
        return playerSuffixMap[info.gameProfile.id] ?: ""
    }
}