package nearlmod.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.cards.friendcards.*;

public class Whislash extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Whislash";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/whislash.png";
    public static boolean uniqueUsed;

    public Whislash(int amount) {
        super(ORB_ID, NAME, 0, 0, DESCRIPTION[0], "", IMAGE, amount);
        uniqueUsed = false;
        updateDescription();
    }

    public Whislash() {
        this(0);
    }

    @Override
    public void applyStrength(int amount) {
        super.applyStrength(amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTION[upgraded? 2 : 0] + passiveAmount + DESCRIPTION[1];
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Whislash();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded, boolean notUnique) {
        int random = AbstractDungeon.cardRng.random(notUnique? 1 : 0, 3);
        AbstractFriendCard card;
        switch (random) {
            case 0:
                card = new Rebuke();
                break;
            case 1:
                card = new VisionofUnity();
                break;
            case 2:
                card = new MotivationalSkill();
                break;
            default:
                card = new WhipSword();
        }
        if (upgraded) card.upgrade();
        return card;
    }

    @Override
    public void onStartOfTurn() {
        AbstractDungeon.player.hand.addToHand(getRandomCard(upgraded, uniqueUsed));
    }
}
