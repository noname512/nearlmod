package nearlmod.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.cards.friendcards.AllySupport;
import nearlmod.cards.friendcards.FeatherShineArrows;

public class Fartooth extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Fartooth";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/fartooth.png";

    public Fartooth(int amount) {
        super(ORB_ID, NAME, 0, 0, DESCRIPTION[0], "", IMAGE, amount);
        updateDescription();
    }

    public Fartooth() {
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
        return new Fartooth();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded) {
        int random = AbstractDungeon.cardRng.random(0, 1);
        AbstractFriendCard card;
        if (random == 0)
            card = new AllySupport();
        else
            card = new FeatherShineArrows();
        if (upgraded) card.upgrade();
        return card;
    }

    @Override
    public void onStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new AddFriendCardToHandAction(getRandomCard(upgraded)));
    }
}
