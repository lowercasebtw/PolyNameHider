package btw.lowercase.namehider.handlers

import btw.lowercase.namehider.config.NameHiderConfig
import btw.lowercase.namehider.config.SuffixBackupType
import btw.lowercase.namehider.util.DictionaryUtil
import net.minecraft.client.network.NetworkPlayerInfo

object NameHandler {
    private var lastId = 0
    private val suffixMap = HashMap<NetworkPlayerInfo, String>()

    @JvmStatic
    fun getNickname(info: NetworkPlayerInfo): String {
        val prefix = this.getPrefix()
        val suffix = this.getSuffix(info)
        return "$prefix-$suffix"
    }

    private fun getPrefix(): String {
        return NameHiderConfig.INSTANCE.namePrefix.trim().ifEmpty { "Player" }
    }

    private fun getSuffix(info: NetworkPlayerInfo): String {
        if (!this.suffixMap.containsKey(info)) {
            this.assignSuffix(info)
        }

        return this.suffixMap[info] ?: "error"
    }

    private fun assignSuffix(info: NetworkPlayerInfo) {
        this.suffixMap[info] = NameHiderConfig.INSTANCE.nameSuffix.trim().ifEmpty {
            when (NameHiderConfig.INSTANCE.suffixBackupType()) {
                SuffixBackupType.COUNTER -> "${this.lastId++}"
                SuffixBackupType.RANDOM_WORD -> DictionaryUtil.random()
            }
        }
    }

    @JvmStatic
    fun clear() {
        this.lastId = 0
        this.suffixMap.clear()
    }
}