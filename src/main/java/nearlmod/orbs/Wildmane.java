package nearlmod.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.cards.friendcards.*;

public class Wildmane extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Wildmane";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/wildmane.png";
    public boolean upgraded;

    public Wildmane(int amount) {
        super(ORB_ID, NAME, 0, 0, DESCRIPTION[0], "", IMAGE);
        passiveAmount = amount;
        upgraded = false;
        updateDescription();
    }

    public Wildmane() {
        this(0);
    }

    @Override
    public void applyStrength(int amount) {
        super.applyStrength(amount);
        updateDescription();
    }

    public void upgrade() {
        upgraded = true;
        applyStrength(2);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTION[upgraded? 2 : 0] + passiveAmount + DESCRIPTION[1];
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Wildmane();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded) {
        int random = AbstractDungeon.cardRng.random(0, 1);
        AbstractFriendCard card;
        if (random == 0)
            card = new StabbingLance();
        else
            card = new LanceCharge();
        if (upgraded) card.upgrade();
        return card;
    }

    @Override
    public void onStartOfTurn() {
        AbstractDungeon.player.hand.addToHand(getRandomCard(upgraded));
    }
}
