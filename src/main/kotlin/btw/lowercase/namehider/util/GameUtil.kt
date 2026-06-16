package btw.lowercase.namehider.util

import com.mojang.authlib.GameProfile
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.entity.Entity

object GameUtil {
    @JvmField
    val client: Minecraft = Minecraft.getMinecraft()

    @JvmField
    val localPlayer: EntityPlayerSP? = client.thePlayer

    @JvmField
    val localProfile: GameProfile = client.session.profile

    @JvmField
    val localPlayerInfo: NetworkPlayerInfo = NetworkPlayerInfo(localProfile)

    @JvmStatic
    fun isSelf(entity: Entity) = entity == localPlayer
}