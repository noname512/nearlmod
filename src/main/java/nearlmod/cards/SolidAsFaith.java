package nearlmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.SummonFriendAction;
import nearlmod.orbs.Penance;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

public class SolidAsFaith extends AbstractNearlCard {
    public static final String ID = "nearlmod:SolidAsFaith";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/solidasfaith.png";
    private static final int COST = 3;
    private static final int TEMP_HP_AMT = 8;
    private static final int UPGRADE_PLUS_AMT = 3;

    public SolidAsFaith() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = TEMP_HP_AMT;
        exhaust = true;
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Penance.ORB_ID;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Penance.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = magicNumber;
        if (extraTriggered()) {
            amount = amount * 2 + TempHPField.tempHp.get(p);
        } else {
            addToBot(new SummonFriendAction(new Penance()));
        }
        addToBot(new AddTemporaryHPAction(p, p, amount));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SolidAsFaith();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_AMT);
            initializeDescription();
        }
    }
}
