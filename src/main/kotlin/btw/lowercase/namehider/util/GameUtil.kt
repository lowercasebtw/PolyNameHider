package btw.lowercase.namehider.util

import com.mojang.authlib.GameProfile
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.network.NetworkPlayerInfo

object GameUtil {
    @JvmField
    val client: Minecraft = Minecraft.getMinecraft()

    @JvmField
    val localProfile: GameProfile = client.session.profile

    @JvmField
    val localPlayerInfo: NetworkPlayerInfo = NetworkPlayerInfo(localProfile)

    @JvmStatic
    fun isSelf(entity: AbstractClientPlayer) = entity.gameProfile == localProfile
}