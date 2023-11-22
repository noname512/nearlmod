package nearlmod.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;

import java.util.ArrayList;

public class Ashlock extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Ashlock";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/ashlock.png";

    public Ashlock(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
        MY_X_OFFSET = 20.0F * Settings.scale;
    }

    public Ashlock() {
        this(0);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Ashlock();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded) {
        ArrayList<AbstractFriendCard> cards = new ArrayList<>();
        cards.add(new FocusedBombardment());
        cards.add(new BombardmentStudies());
        return getRandomCard(cards, upgraded);
    }

    @Override
    public void onStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new AddFriendCardToHandAction(getRandomCard(upgraded)));
    }
}
