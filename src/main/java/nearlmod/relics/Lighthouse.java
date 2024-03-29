package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Lighthouse extends CustomRelic {

    public static final String ID = "nearlmod:Lighthouse";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/nearlmod/images/relics/lighthouse.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/nearlmod/images/relics/lighthouse_p.png");
    public static boolean isFirstTime;
    public Lighthouse() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.BOSS, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        isFirstTime = true;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Lighthouse();
    }
}
