package nearlmod.orbs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;

import java.util.ArrayList;

public class Aurora extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Aurora";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "resources/nearlmod/images/orbs/aurora.png";
    public static final String RELAX_IMAGE = "resources/nearlmod/images/orbs/aurora_relax.png";
    public static boolean uniqueUsed;
    public status curStatus;
    public int blockGain;
    public enum status {
        NORMAL, RESPITE
    }

    public Aurora(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
        curStatus = status.NORMAL;
    }

    public Aurora() {
        this(0);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Aurora();
    }

    public static AbstractFriendCard getRandomCard(boolean upgraded, boolean notUnique) {
        ArrayList<AbstractFriendCard> cards = new ArrayList<>();
        if (!notUnique) {
            cards.add(new ShieldPhotographyModule());
        }
        cards.add(new HomelandProtector());
        cards.add(new ArtificialSnowfall());
        cards.add(new FrigidRespite());
        return getRandomCard(cards, upgraded);
    }

    @Override
    public void onStartOfTurn() {
        if (curStatus == status.NORMAL) {
            addToBot(new AddFriendCardToHandAction(getRandomCard(upgraded, uniqueUsed)));
        }
    }

    @Override
    public void onEndOfTurn() {
        if (curStatus == status.RESPITE) {
            addToBot(new GainBlockAction(AbstractDungeon.player, blockGain));
        }
    }

    public void startRespite(int block) {
        curStatus = status.RESPITE;
        blockGain = block;
        img = ImageMaster.loadImage(RELAX_IMAGE);
    }

    public void endRespite() {
        curStatus = status.NORMAL;
        img = ImageMaster.loadImage(IMAGE);
    }

    @Override
    public ArrayList<AbstractCard> getRelateCards() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        return list;
    }
}
