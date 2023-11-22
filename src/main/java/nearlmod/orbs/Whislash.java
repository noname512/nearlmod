package nearlmod.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;

import java.util.ArrayList;

public class Whislash extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Whislash";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/whislash.png";
    public static boolean uniqueUsed;

    public Whislash(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
    }

    public Whislash() {
        this(0);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Whislash();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded, boolean notUnique) {
        ArrayList<AbstractFriendCard> cards = new ArrayList<>();
        if (!notUnique)
            cards.add(new Rebuke());
        cards.add(new VisionOfUnity());
        cards.add(new MotivationalSkill());
        cards.add(new WhipSword());
        return getRandomCard(cards, upgraded);
    }

    @Override
    public void onStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new AddFriendCardToHandAction(getRandomCard(upgraded, uniqueUsed)));
    }
}
