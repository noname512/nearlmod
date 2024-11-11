package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.SummonFriendAction;
import nearlmod.orbs.Gummy;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.HoneyGingerbreadPower;

public class HoneyGingerbread extends AbstractNearlCard {
    public static final String ID = "nearlmod:HoneyGingerbread";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/honeygingerbread.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int EXTRA_BLOCK = 1;

    public HoneyGingerbread() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Gummy.ORB_ID;
        magicNumber = baseMagicNumber = EXTRA_BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (extraTriggered()) {
            addToBot(new ApplyPowerAction(p, p, new HoneyGingerbreadPower(p, magicNumber)));
        } else {
            addToBot(new SummonFriendAction(new Gummy()));
        }
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Gummy.ORB_ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new HoneyGingerbread();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
