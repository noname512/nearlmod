package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.actions.GainCostAction;

public class EmergencyCallBook extends CustomRelic {

    public static final String ID = "nearlmod:EmergencyCallBook";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/emergencycallbook.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/emergencycallbook.png");
    public EmergencyCallBook() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new GainCostAction(1));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EmergencyCallBook();
    }
}
