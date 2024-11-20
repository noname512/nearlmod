package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.SummonFriendAction;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Aurora;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.stances.DefStance;

public class FearNoCold extends AbstractNearlCard {
    public static final String ID = "nearlmod:FearNoCold";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/fearnocold.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    public FearNoCold() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        tags.add(NearlTags.IS_SUMMON_CARD);
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Aurora.ORB_ID;

        previewList = Nearl.getFriendCard(Aurora.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SummonFriendAction(new Aurora()));
        addToBot(new ChangeStanceAction(new DefStance()));
        if (upgraded) {
            AbstractCard c = new CheeseFondue();
            c.selfRetain = true;
            c.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
            c.initializeDescription();
            addToBot(new MakeTempCardInHandAction(c));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FearNoCold();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
