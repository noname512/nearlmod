package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.SummonFriendAction;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Blemishine;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.BladeOfBlazingSunPower;

public class BladeOfBlazingSun extends AbstractNearlCard {
    public static final String ID = "nearlmod:BladeOfBlazingSun";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/bladeofblazingsun.png";
    private static final int COST = 2;
    private static final int STRENGTH_AMT = 2;
    private static final int UPGRADE_PLUS_STR = 1;

    public BladeOfBlazingSun() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = STRENGTH_AMT;
        tags.add(NearlTags.IS_SUMMON_CARD);

        previewList = Nearl.getFriendCard(Blemishine.ORB_ID);
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Blemishine.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (extraTriggered()) {
            addToBot(new ApplyPowerAction(p, p, new BladeOfBlazingSunPower(p, magicNumber)));
        } else {
            addToBot(new SummonFriendAction(new Blemishine()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BladeOfBlazingSun();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STR);
        }
    }
}
