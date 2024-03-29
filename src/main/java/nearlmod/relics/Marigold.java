package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.actions.SummonFriendAction;
import nearlmod.orbs.Viviana;

public class Marigold extends CustomRelic {

    public static final String ID = "nearlmod:Marigold";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/nearlmod/images/relics/marigold.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/nearlmod/images/relics/marigold_p.png");
    public Marigold() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        addToBot(new SummonFriendAction(new Viviana()));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Marigold();
    }
}
