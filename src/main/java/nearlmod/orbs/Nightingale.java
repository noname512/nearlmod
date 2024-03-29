package nearlmod.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;

import java.util.ArrayList;

public class Nightingale extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Nightingale";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "resources/nearlmod/images/orbs/nightingale.png";
    public static boolean uniqueUsed;

    public Nightingale(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
    }

    public Nightingale() {
        this(0);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Nightingale();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded, boolean notUnique) {
        ArrayList<AbstractFriendCard> cards = new ArrayList<>();
        if (!notUnique)
            cards.add(new WhiteFiendProtection());
        cards.add(new ArtsShield());
        cards.add(new Sanctuary());
        cards.add(new ClosedHope());
        return getRandomCard(cards, upgraded);
    }

    @Override
    public void onStartOfTurn() {
        addToBot(new AddFriendCardToHandAction(getRandomCard(upgraded, uniqueUsed)));
    }
}
