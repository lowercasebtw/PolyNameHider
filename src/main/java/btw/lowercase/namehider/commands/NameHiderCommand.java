package btw.lowercase.namehider.commands;

import btw.lowercase.namehider.config.NameHiderConfig;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value = "namehider", aliases = {"nick", "nickhider"}, description = "NameHider")
public final class NameHiderCommand {
    @Main
    public void handle() {
        NameHiderConfig.INSTANCE.openGui();
    }
}
