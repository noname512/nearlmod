package nearlmod.orbs;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.cards.LSSwiftSword;

public class Viviana extends CustomOrb {

    public static final String ORB_ID = "nearlmod:Viviana";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/viviana.png";

    public Viviana() {
        super(ORB_ID, NAME, 0, 0, DESCRIPTION[0], "", IMAGE);
        showEvokeValue = false;

        angle = MathUtils.random(360.0f);
        channelAnimTimer = 0.5f;
    }

    @Override
    public void onEvoke() {}

    @Override
    public void updateDescription() {
        description = DESCRIPTION[0];
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Viviana();
    }

    @Override
    public void onStartOfTurn() {
        int random = (int)AbstractDungeon.cardRng.random(0, 3);
        switch (random) {
            case 0:
            case 1:
            case 2:
            default:
                AbstractDungeon.player.hand.addToHand(new LSSwiftSword());
        }
    }

    @Override
    public void playChannelSFX() {}
}
