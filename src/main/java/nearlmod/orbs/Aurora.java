package nearlmod.orbs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.BladeOfBlazingSun;
import nearlmod.cards.Cooperate;
import nearlmod.cards.StayGold;
import nearlmod.cards.SwordShield;
import nearlmod.cards.friendcards.*;
import nearlmod.powers.FrigidRespitePower;

import java.util.ArrayList;

public class Aurora extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Aurora";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "resources/nearlmod/images/orbs/ashlock.png";
    // TODO: 极光自己的图；如果可以的话低温休憩状态再来一张
    public static boolean uniqueUsed;

    public Aurora(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
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
        if (!AbstractDungeon.player.hasPower(FrigidRespitePower.POWER_ID)) {
            addToBot(new AddFriendCardToHandAction(getRandomCard(upgraded, uniqueUsed)));
        }
    }

    @Override
    public ArrayList<AbstractCard> getRelateCards() {
        ArrayList<AbstractCard> list = new ArrayList<AbstractCard>();
        return list;
    }
}
