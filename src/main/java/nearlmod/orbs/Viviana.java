package nearlmod.orbs;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;

import java.util.ArrayList;

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
        if (AbstractDungeon.player.hasPower("GlimmeringTouchPower")) {
            startCharging(AbstractDungeon.player.getPower("GlimmeringTouchPower").amount);
        }
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
        ArrayList<AbstractFriendCard> cards = new ArrayList<>();
        if (!notUnique)
            cards.add(new FlameShadow());
        cards.add(new FlashFade());
        cards.add(new GlimmeringTouch());
        cards.add(new LSSwiftSword());
        return getRandomCard(cards, upgraded);
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
