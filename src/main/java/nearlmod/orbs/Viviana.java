package nearlmod.orbs;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.cards.LSSwiftSword;
import nearlmod.patches.NearlTags;

import java.util.ArrayList;
import java.util.Iterator;

public class Viviana extends CustomOrb {

    public static final String ORB_ID = "nearlmod:Viviana";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/viviana.png";

    public Viviana(int amount) {
        super(ORB_ID, NAME, 0, 0, DESCRIPTION[0], "", IMAGE);

        showEvokeValue = false;
        angle = MathUtils.random(360.0f);
        channelAnimTimer = 0.5f;
        passiveAmount = amount;
        updateDescription();
    }

    public Viviana() {
        this(0);
    }

    public void applyStrength(int amount) {
        passiveAmount += amount;
        ArrayList<AbstractCard> cardSet = AbstractDungeon.player.hand.group;
        Iterator it = cardSet.iterator();
        while (it.hasNext()) {
            AbstractCard card = (AbstractCard)it.next();
            if (card instanceof AbstractNearlCard)
                if (((AbstractNearlCard) card).hasTag(NearlTags.IS_FRIEND_CARD) &&
                    ((AbstractNearlCard) card).belongFriend.equals(ORB_ID)) {
                    ((AbstractNearlCard) card).applyFriendPower(amount);
                }
        }
        updateDescription();
    }

    @Override
    public void onEvoke() {}

    @Override
    public void applyFocus() {}

//    @Override
//    public void updateAnimation() {}

    @Override
    public void updateDescription() {
        description = DESCRIPTION[0] + passiveAmount + DESCRIPTION[1];
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
