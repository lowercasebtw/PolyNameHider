package btw.lowercase.namehider

import btw.lowercase.namehider.commands.NameHiderCommand
import btw.lowercase.namehider.config.NameHiderConfig
import btw.lowercase.namehider.util.DictionaryUtil
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.utils.commands.CommandManager
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(
    modid = NameHider.MOD_ID,
    name = NameHider.NAME,
    version = NameHider.VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter"
)
object NameHider {
    const val MOD_ID: String = "@ID@"
    const val NAME: String = "@NAME@"
    const val VERSION: String = "@VER@"

    @JvmField
    val MOD = cc.polyfrost.oneconfig.config.data.Mod(NAME, ModType.UTIL_QOL, "/${MOD_ID}.svg")

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        NameHiderConfig.INSTANCE.preload()
        CommandManager.INSTANCE.registerCommand(NameHiderCommand())
        EventManager.INSTANCE.register(this)
        DictionaryUtil.load()
    }
}