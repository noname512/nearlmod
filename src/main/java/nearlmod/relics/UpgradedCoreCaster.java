package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class UpgradedCoreCaster extends CustomRelic {

    public static final String ID = "nearlmod:UpgradedCoreCaster";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/upgradedcorecaster.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/upgradedcorecaster_p.png");
    public static final int EXTRA_VAL = 4;
    public UpgradedCoreCaster() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new UpgradedCoreCaster();
    }
}
