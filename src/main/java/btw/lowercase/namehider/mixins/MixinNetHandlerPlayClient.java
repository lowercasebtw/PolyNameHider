package btw.lowercase.namehider.mixins;

import btw.lowercase.namehider.handlers.NameHandler;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void namehider$reset(final IChatComponent reason, final CallbackInfo ci) {
        NameHandler.clearPlayerSuffixes();
    }
}
