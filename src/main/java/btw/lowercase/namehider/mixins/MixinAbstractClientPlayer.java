package btw.lowercase.namehider.mixins;

import btw.lowercase.namehider.config.NameHiderConfig;
import btw.lowercase.namehider.mixins.accessor.AbstractClientPlayerAccessor;
import btw.lowercase.namehider.util.GameUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {
    @Redirect(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getPlayerInfo()Lnet/minecraft/client/network/NetworkPlayerInfo;"))
    private NetworkPlayerInfo namehider$hideSkins(final AbstractClientPlayer instance) {
        if (NameHiderConfig.INSTANCE.enabled) {
            final boolean isSelf = GameUtil.isSelf(instance);
            if (NameHiderConfig.INSTANCE.hideYourSkin && isSelf) {
                return null;
            } else if (NameHiderConfig.INSTANCE.hideOthersSkin && !isSelf) {
                return NameHiderConfig.INSTANCE.everyoneIsYou ? GameUtil.localPlayerInfo : null;
            }
        }

        return ((AbstractClientPlayerAccessor) instance).namehider$getPlayerInfo();
    }
}
