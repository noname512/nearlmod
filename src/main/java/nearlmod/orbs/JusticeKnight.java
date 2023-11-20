package nearlmod.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;

public class JusticeKnight extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:JusticeKnight";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/justiceknight.png";

    public JusticeKnight(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
        MY_X_OFFSET = 70.0F * Settings.scale;
    }

    public JusticeKnight() {
        this(0);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new JusticeKnight();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded) {
        int random = AbstractDungeon.cardRng.random(0, 1);
        AbstractFriendCard card;
        if (random == 0)
            card = new JusticeDrive();
        else
            card = new BeepActivate();
        if (upgraded) card.upgrade();
        return card;
    }

    @Override
    public void onStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new AddFriendCardToHandAction(getRandomCard(upgraded)));
    }
}
