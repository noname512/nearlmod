package nearlmod.orbs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.Duelist;
import nearlmod.cards.FearNoCold;
import nearlmod.cards.WarmthOfHome;
import nearlmod.cards.WhatSheSaw;
import nearlmod.cards.friendcards.*;

import java.util.ArrayList;

public class Aurora extends AbstractFriend {

    public static final String ORB_ID = "nearlmod:Aurora";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String NAME = orbStrings.NAME;
    public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
    public static final String IMAGE = "resources/nearlmod/images/orbs/aurora.png";
    public static final String RELAX_IMAGE = "resources/nearlmod/images/orbs/aurora_relax.png";
    public status curStatus;
    public int blockGain;
    public enum status {
        NORMAL, RESPITE
    }

    public Aurora(int amount) {
        super(ORB_ID, NAME, DESCRIPTION, IMAGE, amount);
        curStatus = status.NORMAL;
        blockGain = 0;
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
    public AbstractFriendCard getUniqueCard() {
        return new ShieldPhotographyModule();
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
            addToBot(new GainBlockAction(AbstractDungeon.player, blockGain + trustAmount));
        }
    }

    public void startRespite(int block) { // TODO:参考黑球，左下角显示叠甲数
        curStatus = status.RESPITE;
        blockGain += block;
        img = ImageMaster.loadImage(RELAX_IMAGE);
    }

    public void endRespite() {
        curStatus = status.NORMAL;
        blockGain = 0;
        img = ImageMaster.loadImage(IMAGE);
    }

    @Override
    public ArrayList<AbstractCard> getRelateCards() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new FearNoCold());
        list.add(new WarmthOfHome());
        list.add(new WhatSheSaw());
        list.add(new Duelist());
        return list;
    }
}
