package btw.lowercase.namehider.handlers

import btw.lowercase.namehider.config.NameHiderConfig
import btw.lowercase.namehider.config.SuffixBackupType
import btw.lowercase.namehider.util.DictionaryUtil
import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.client.network.NetworkPlayerInfo
import java.util.regex.Pattern

// TODO/NOTE: What happens if someone gets assigned the same random dictionary word?
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

    @JvmStatic
    fun replaceHiddenNames(input: String, name: String, info: NetworkPlayerInfo): String {
        return createNameRegex(ChatFormatting.stripFormatting(name))
            .matcher(input)
            .replaceAll(this.getNickname(info));
    }

    private fun createNameRegex(input: String?): Pattern {
        if (input == null) {
            throw RuntimeException("Input must not be null to build nick regex!")
        } else {
            val builder = StringBuilder()
            for (index in input.indices) {
                if (index > 0) {
                    builder.append("(?:§.)*")
                }

                builder.append(Pattern.quote(input[index].toString()))
            }

            return Pattern.compile(builder.toString(), Pattern.CASE_INSENSITIVE)
        }
    }
}