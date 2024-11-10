package nearlmod.orbs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.*;
import nearlmod.cards.friendcards.*;

import java.util.ArrayList;

public class Gummy extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Gummy";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "resources/nearlmod/images/orbs/gummy.png";

    public Gummy(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
    }

    public Gummy() {
        this(0);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Gummy();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded, boolean notUnique) {
        ArrayList<AbstractFriendCard> cards = new ArrayList<>();
        if (!notUnique) {
            cards.add(new Cooking());
        }
        //cards.add(new FlareGrenade());
        cards.add(new Specialty());
        //cards.add(new UltimateLineOfDefense());
        return getRandomCard(cards, upgraded);
    }

    @Override
    public AbstractFriendCard getUniqueCard() {
        return new Cooking();
    }

    @Override
    public void onStartOfTurn() {
        addToBot(new AddFriendCardToHandAction(getRandomCard(upgraded, uniqueUsed)));
    }

    @Override
    public ArrayList<AbstractCard> getRelateCards() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new BattlefieldCulinarian());
        list.add(new Provisions());
        list.add(new UrsusRoar());
        //list.add(new FallingShield());
        return list;
    }
}
