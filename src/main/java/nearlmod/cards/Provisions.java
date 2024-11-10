package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.SummonFriendAction;
import nearlmod.cards.special.Dishes;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Aurora;
import nearlmod.orbs.Gummy;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.DeliciousPower;

public class Provisions extends AbstractNearlCard {
    public static final String ID = "nearlmod:Provisions";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/provisions.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 3;
    private static final int DRAW_AMT = 1;
    private static final int DISH_AMT = 2;
    private static final int UPGRADE_BLOCK_AMT = 1;

    public Provisions() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.SELF);
        tags.add(NearlTags.FRIEND_RELATED);
        tags.add(NearlTags.IS_FOOD);
        belongFriend = Gummy.ORB_ID;
        cardsToPreview = new Dishes();
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = DRAW_AMT;
        secondMagicNumber = baseSecondMagicNumber = DISH_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(new DrawCardAction(p, magicNumber));
        if (extraTriggered()) {
            addToBot(new MakeTempCardInDrawPileAction(new Dishes(), secondMagicNumber, true, true));
        }
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Gummy.ORB_ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Provisions();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK_AMT);
            cardsToPreview.upgrade();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
