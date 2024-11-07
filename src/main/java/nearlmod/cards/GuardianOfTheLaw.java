package nearlmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.SummonFriendAction;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Penance;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.util.CostReserves;

public class GuardianOfTheLaw extends AbstractNearlCard {
    public static final String ID = "nearlmod:GuardianOfTheLaw";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/guardianofthelaw.png";
    private static final int COST = 2;
    private static final int TEMP_HP_TIMES = 4;
    private static final int UPGRADE_COST = 1;
    private static final int UPGRADE_PLUS_TIMES = 1;

    public GuardianOfTheLaw() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = TEMP_HP_TIMES;
        tags.add(NearlTags.IS_SUMMON_CARD);
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Penance.ORB_ID;

        previewList = Nearl.getFriendCard(Penance.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SummonFriendAction(new Penance()));
        int amount = CostReserves.reserveCount();
        CostReserves.resetReserves();
        addToBot(new AddTemporaryHPAction(p, p, amount * magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GuardianOfTheLaw();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            upgradeMagicNumber(UPGRADE_PLUS_TIMES);
        }
    }
}
