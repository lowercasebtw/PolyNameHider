package btw.lowercase.namehider.mixins.accessor;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractClientPlayer.class)
public interface AbstractClientPlayerAccessor {
    @Invoker("getPlayerInfo")
    NetworkPlayerInfo namehider$getPlayerInfo();
}
