package btw.lowercase.namehider.mixins;

import btw.lowercase.namehider.handlers.NameHandler;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @Inject(method = "handlePlayerListItem", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void namehider$assignPlayerNick(final S38PacketPlayerListItem packetIn, final CallbackInfo ci, final Iterator<?> var2, final S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata, final NetworkPlayerInfo networkplayerinfo) {
        if (networkplayerinfo != null) {
            NameHandler.assignPlayerSuffix(networkplayerinfo);
        }
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void namehider$reset(final IChatComponent reason, final CallbackInfo ci) {
        NameHandler.clearPlayerSuffixes();
    }
}
