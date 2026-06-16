package btw.lowercase.namehider.config;

import btw.lowercase.namehider.NameHider;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Exclude;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;

public class NameHiderConfig extends Config {
    // Name
    @Switch(
            name = "Hide Your Name",
            description = "Hides your name.",
            subcategory = "Name"
    )
    public boolean hideYourName = false;

    @Switch(
            name = "Hide Others Name",
            description = "Hides other players names.",
            subcategory = "Name"
    )
    public boolean hideOthersName = false;

    @Text(
            name = "Nick Name Prefix",
            description = "Text that shows before the nicked name.",
            subcategory = "Name",
            placeholder = "Name"
    )
    public String namePrefix = "Player";

    @Text(
            name = "Nick Name Suffix",
            description = "Text that shows after the nicked name.",
            subcategory = "Name"
    )
    public String nameSuffix = "";

    @Dropdown(
            name = "Suffix Backup Type",
            description = "The type of replacement to show for suffix when it is empty.",
            subcategory = "Name",
            options = {"Counter", "Random Word"}
    )
    private int suffixBackupType = SuffixBackupType.COUNTER.ordinal();

    public SuffixBackupType suffixBackupType() {
        return SuffixBackupType.VALUES[this.suffixBackupType % (SuffixBackupType.VALUES.length + 1)];
    }

    // Skins
    @Switch(
            name = "Hide Your Skin",
            description = "Forces your skin to render as Steve/Alex.",
            subcategory = "Skins"
    )
    public boolean hideYourSkin = false;

    @Switch(
            name = "Hide Others Skins",
            description = "Forces all other players skin to render as Steve/Alex.",
            subcategory = "Skins"
    )
    public boolean hideOthersSkin = false;

    @Switch(
            name = "Everyone Is You",
            description = "Forces all other players skin to render as your own skin.",
            subcategory = "Skins"
    )
    public boolean everyoneIsYou = false;

    @Exclude
    public static final NameHiderConfig INSTANCE = new NameHiderConfig();

    public NameHiderConfig() {
        super(NameHider.MOD, NameHider.MOD_ID + ".json");
        this.initialize();

        // Skins
        this.addDependency("everyoneIsYou", "hideOthersSkin");
    }
}
