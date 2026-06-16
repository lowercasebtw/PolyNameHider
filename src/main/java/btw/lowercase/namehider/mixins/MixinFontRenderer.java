package btw.lowercase.namehider.mixins;

import btw.lowercase.namehider.config.NameHiderConfig;
import btw.lowercase.namehider.handlers.NameHandler;
import btw.lowercase.namehider.mixins.accessor.NetHandlerPlayClientAccessor;
import btw.lowercase.namehider.util.GameUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {
    @ModifyVariable(method = "renderStringAtPos", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String namehider$hideNames(final String input) {
        if (NameHiderConfig.INSTANCE.enabled && (NameHiderConfig.INSTANCE.hideYourName || NameHiderConfig.INSTANCE.hideOthersName)) {
            return namehider$inspectAndModify(input);
        } else {
            return input;
        }
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String namehider$hiddenNameStringWidth(final String input) {
        if (NameHiderConfig.INSTANCE.enabled && (NameHiderConfig.INSTANCE.hideYourName || NameHiderConfig.INSTANCE.hideOthersName)) {
            return namehider$inspectAndModify(input);
        } else {
            return input;
        }
    }

    @Unique
    private String namehider$inspectAndModify(final String input) {
        if (input == null) {
            return null; // Abort
        } else {
            String text = input;
            if (NameHiderConfig.INSTANCE.hideYourName) {
                text = NameHandler.replaceHiddenNames(text, GameUtil.localProfile.getName(), GameUtil.localPlayerInfo);
            }

            final NetHandlerPlayClient netHandler = GameUtil.client.getNetHandler();
            if (NameHiderConfig.INSTANCE.hideOthersName && netHandler != null) {
                for (final NetworkPlayerInfo info : ((NetHandlerPlayClientAccessor) netHandler).namehider$getPlayerInfoMap().values()) {
                    try {
                        // TODO/FIX: Null
                        text = NameHandler.replaceHiddenNames(text, info.getDisplayName().getFormattedText(), info);
                    } catch (final Exception exception) {
                    }
                }
            }

            return text;
        }
    }
}
