package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.actions.SummonFriendAction;
import nearlmod.cards.special.Dishes;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Gummy;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.DeliciousPower;
import nearlmod.relics.NormalPerson;

public class BattlefieldCulinarian extends AbstractNearlCard {
    public static final String ID = "nearlmod:BattlefieldCulinarian";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/battlefieldculinarian.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int DISHES_NUM = 3;
    private static final int DELICIOUS_POWER = 2;

    public BattlefieldCulinarian() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        tags.add(NearlTags.IS_SUMMON_CARD);
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Gummy.ORB_ID;
        cardsToPreview = new Dishes();
        magicNumber = baseMagicNumber = DISHES_NUM;
        secondMagicNumber = baseSecondMagicNumber = DELICIOUS_POWER;

        previewList = Nearl.getFriendCard(Gummy.ORB_ID);
        previewList.add(0, new Dishes());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInDrawPileAction(new Dishes(), magicNumber, true, true));
        addToBot(new SummonFriendAction(new Gummy()));
        if (upgraded) {
            addToBot(new ApplyPowerAction(p, p, new DeliciousPower(p, secondMagicNumber)));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BattlefieldCulinarian();
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
