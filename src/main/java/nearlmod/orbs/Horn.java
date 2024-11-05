package nearlmod.orbs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.Exemplars;
import nearlmod.cards.FallingShield;
import nearlmod.cards.Solo;
import nearlmod.cards.TempestPlatoon;
import nearlmod.cards.friendcards.*;

import java.util.ArrayList;

public class Horn extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Horn";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "resources/nearlmod/images/orbs/horn.png";
    public static boolean uniqueUsed;

    public Horn(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
    }

    public Horn() {
        this(0);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Horn();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded, boolean notUnique) {
        ArrayList<AbstractFriendCard> cards = new ArrayList<>();
        if (!notUnique) {
            cards.add(new Bloodbath());
        }
        cards.add(new FlareGrenade());
        cards.add(new TempestCommand());
        cards.add(new UltimateLineOfDefense());
        return getRandomCard(cards, upgraded);
    }

    @Override
    public void onStartOfTurn() {
        addToBot(new AddFriendCardToHandAction(getRandomCard(upgraded, uniqueUsed)));
    }

    @Override
    public ArrayList<AbstractCard> getRelateCards() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new TempestPlatoon());
        list.add(new Exemplars());
        list.add(new Solo());
        list.add(new FallingShield());
        return list;
    }
}
