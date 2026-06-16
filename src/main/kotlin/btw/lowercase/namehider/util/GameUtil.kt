package btw.lowercase.namehider.util

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
    val localUsername = localPlayer?.name ?: ""

    @JvmField
    val localPlayerInfo: NetworkPlayerInfo = NetworkPlayerInfo(client.session.profile)

    @JvmStatic
    fun isSelf(entity: Entity) = entity.entityId == localPlayer?.entityId
}