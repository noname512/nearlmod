package nearlmod.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;

import java.util.ArrayList;

public class Flametail extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Flametail";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/flametail.png";

    public Flametail(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
    }

    public Flametail() {
        this(0);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Flametail();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded) {
        ArrayList<AbstractFriendCard> cards = new ArrayList<>();
        cards.add(new PinusSylvestris());
        cards.add(new FlameHeart());
        return getRandomCard(cards, upgraded);
    }

    @Override
    public void onStartOfTurn() {
        addToBot(new AddFriendCardToHandAction(getRandomCard(upgraded)));
    }
}
