package nearlmod.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;

public class Nightingale extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Nightingale";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/nightingale.png";
    public static boolean uniqueUsed;

    public Nightingale(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
        uniqueUsed = false;
    }

    public Nightingale() {
        this(0);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Nightingale();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded, boolean notUnique) {
        int random = AbstractDungeon.cardRng.random(notUnique? 1 : 0, 3);
        AbstractFriendCard card;
        switch (random) {
            case 0:
                card = new WhiteFiendProtection();
                break;
            case 1:
                card = new ArtsShield();
                break;
            case 2:
                card = new Sanctuary();
                break;
            default:
                card = new ClosedHope();
        }
        if (upgraded) card.upgrade();
        return card;
    }

    @Override
    public void onStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new AddFriendCardToHandAction(getRandomCard(upgraded, uniqueUsed)));
    }
}
