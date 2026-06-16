package btw.lowercase.namehider.mixins;

import btw.lowercase.namehider.config.NameHiderConfig;
import btw.lowercase.namehider.handlers.NameHandler;
import btw.lowercase.namehider.mixins.accessor.NetHandlerPlayClientAccessor;
import btw.lowercase.namehider.util.GameUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.regex.Pattern;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {
    @ModifyVariable(method = "renderStringAtPos", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String namehider$hideChatNames(final String input) {
        return namehider$inspectAndModify(input);
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String namehider$nickStringWidth(final String input) {
        return namehider$inspectAndModify(input);
    }

    @Unique
    private String namehider$inspectAndModify(final String input) {
        String text = input;
        if (NameHiderConfig.INSTANCE.enabled) {
            if (NameHiderConfig.INSTANCE.hideYourName) {
                text = namehider$format(text, GameUtil.localProfile.getName(), GameUtil.localPlayerInfo);
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
        return namehider$nameRegex(ChatFormatting.stripFormatting(name)).matcher(input).replaceAll(NameHandler.getNickname(info));
    }

    @Unique
    private Pattern namehider$nameRegex(final String input) {
        final StringBuilder builder = new StringBuilder();
        for (int index = 0; index < input.length(); index++) {
            if (index > 0) {
                builder.append("(?:§.)*");
            }

            builder.append(Pattern.quote(String.valueOf(input.charAt(index))));
        }

        return Pattern.compile(builder.toString(), Pattern.CASE_INSENSITIVE);
    }
}
