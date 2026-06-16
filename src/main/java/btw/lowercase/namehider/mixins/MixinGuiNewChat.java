package btw.lowercase.namehider.mixins;

import btw.lowercase.namehider.config.NameHiderConfig;
import btw.lowercase.namehider.handlers.NameHandler;
import btw.lowercase.namehider.mixins.accessor.NetHandlerPlayClientAccessor;
import btw.lowercase.namehider.util.GameUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {
    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/IChatComponent;getFormattedText()Ljava/lang/String;"))
    private String namehider$hideChatNames(final IChatComponent instance) {
        String text = instance.getFormattedText();
        if (NameHiderConfig.INSTANCE.enabled) {
            if (NameHiderConfig.INSTANCE.hideYourName) {
                text = namehider$format(text, GameUtil.localUsername, GameUtil.localPlayerInfo);
            }

            final NetHandlerPlayClient netHandler = GameUtil.client.getNetHandler();
            if (NameHiderConfig.INSTANCE.hideOthersName && netHandler != null) {
                for (final NetworkPlayerInfo info : ((NetHandlerPlayClientAccessor) netHandler).namehider$getPlayerInfoMap().values()) {
                    text = namehider$format(text, info.getDisplayName().getFormattedText(), info);
                }
            }
        }

        return text;
    }

    @Unique
    private String namehider$format(final String input, final String name, final NetworkPlayerInfo info) {
        final String unformattedName = ChatFormatting.stripFormatting(name);
        final String unformattedInput = ChatFormatting.stripFormatting(input);

        final int indexOf = unformattedInput.indexOf(name);
        if (indexOf != -1) {
            return unformattedInput.replaceAll(unformattedName, NameHandler.getNickname(info));
        } else {
            return input;
        }
    }
}
