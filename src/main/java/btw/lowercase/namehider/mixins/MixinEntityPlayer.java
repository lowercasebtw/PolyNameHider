package btw.lowercase.namehider.mixins;

import btw.lowercase.namehider.config.NameHiderConfig;
import btw.lowercase.namehider.handlers.NameHandler;
import btw.lowercase.namehider.mixins.accessor.AbstractClientPlayerAccessor;
import btw.lowercase.namehider.util.GameUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer {
    @Inject(method = "getDisplayNameString", at = @At("RETURN"), cancellable = true, remap = false)
    private void nmehider$hideName(final CallbackInfoReturnable<String> cir) {
        final EntityPlayer thiz = (EntityPlayer) (Object) this;
        if (NameHiderConfig.INSTANCE.enabled && thiz instanceof AbstractClientPlayer) {
            final AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) thiz;
            final boolean isSelf = GameUtil.isSelf(abstractClientPlayer);
            if ((NameHiderConfig.INSTANCE.hideYourName && isSelf) || (NameHiderConfig.INSTANCE.hideOthersName && !isSelf)) {
                cir.setReturnValue(NameHandler.getNickname(((AbstractClientPlayerAccessor) abstractClientPlayer).namehider$getPlayerInfo()));
            }
        }
    }
}
