package nearlmod.orbs;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.cards.friendcards.*;

public class Nightingale extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Nightingale";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "images/orbs/nightingale.png";
    public boolean upgraded;

    public Nightingale(int amount) {
        super(ORB_ID, NAME, 0, 0, DESCRIPTION[0], "", IMAGE);

        showEvokeValue = false;
        angle = MathUtils.random(360.0f);
        channelAnimTimer = 0.5f;
        passiveAmount = amount;
        upgraded = false;
        updateDescription();
    }

    public Nightingale() {
        this(0);
    }

    @Override
    public void applyStrength(int amount) {
        super.applyStrength(amount);
        updateDescription();
    }

    public void upgrade() {
        upgraded = true;
        applyStrength(2);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTION[upgraded? 2 : 0] + passiveAmount + DESCRIPTION[1];
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Nightingale();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded, boolean notUnique) {
        int random = AbstractDungeon.cardRng.random(notUnique? 1 : 0, 3);
        AbstractFriendCard card;
        switch (random) {
            case 0:
                card = new WhiteFiendProtection();
                break;
            case 1:
                card = new ArtsShield();
                break;
            case 2:
                card = new Sanctuary();
                break;
            default:
                card = new FleetingPhantom();
        }
        if (upgraded) card.upgrade();
        return card;
    }

    @Override
    public void onStartOfTurn() {
        AbstractDungeon.player.hand.addToHand(getRandomCard(upgraded, uniqueUsed));
    }
}
