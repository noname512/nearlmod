package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RatSwarm extends CustomRelic {

    public static final String ID = "nearlmod:RatSwarm";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/ratswarm.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/ratswarm_p.png");
    public static boolean isFirstTime;
    public RatSwarm() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SHOP, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {}

    @Override
    public AbstractRelic makeCopy() {
        return new RatSwarm();
    }
}
