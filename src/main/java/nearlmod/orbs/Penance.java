package nearlmod.orbs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.*;
import nearlmod.cards.friendcards.*;

import java.util.ArrayList;

public class Penance extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Penance";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "resources/nearlmod/images/orbs/penance.png";

    public Penance(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
    }

    public Penance() {
        this(0);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Penance();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded, boolean notUnique) {
        ArrayList<AbstractFriendCard> cards = new ArrayList<>();
        if (!notUnique) {
            cards.add(new TrialOfThorns());
        }
        cards.add(new WreathedInThorns());
        cards.add(new LastWord());
        cards.add(new StoicAtonement());
        return getRandomCard(cards, upgraded);
    }

    @Override
    public void onStartOfTurn() {
        addToBot(new AddFriendCardToHandAction(getRandomCard(upgraded, uniqueUsed)));
    }

    @Override
    public ArrayList<AbstractCard> getRelateCards() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new GuardianOfTheLaw());
        list.add(new Revolution());
        list.add(new Innocent());
        list.add(new SolidAsFaith());
        return list;
    }
}
