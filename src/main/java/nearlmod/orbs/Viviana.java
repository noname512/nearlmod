package nearlmod.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;

public class Viviana extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Viviana";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/viviana.png";
    public static final String CHARGINGIMAGE = "images/orbs/viviana_charging.png";
    public static boolean uniqueUsed;
    public static int chargingTurn;

    public Viviana(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
        chargingTurn = 0;
    }

    public Viviana() {
        this(0);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Viviana();
    }

    public void startCharging(int turns) {
        chargingTurn = turns;
        img = ImageMaster.loadImage(CHARGINGIMAGE);
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded, boolean notUnique) {
        int random = AbstractDungeon.cardRng.random(notUnique? 1 : 0, 3);
        AbstractFriendCard card;
        switch (random) {
            case 0:
                card = new FlameShadow();
                break;
            case 1:
                card = new FlashFade();
                break;
            case 2:
                card = new GlimmeringTouch();
                break;
            default:
                card = new LSSwiftSword();
        }
        if (upgraded) card.upgrade();
        return card;
    }

    @Override
    public void onStartOfTurn() {
        if (chargingTurn > 0) {
            chargingTurn--;
            if (chargingTurn == 0)
                img = ImageMaster.loadImage(IMAGE);
            return;
        }
        AbstractDungeon.actionManager.addToBottom(new AddFriendCardToHandAction(getRandomCard(upgraded, uniqueUsed)));
    }
}
